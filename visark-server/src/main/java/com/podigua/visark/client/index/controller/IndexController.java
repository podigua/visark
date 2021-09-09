package com.podigua.visark.client.index.controller;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.podigua.javafx.State;
import com.podigua.javafx.support.FxmlService;
import com.podigua.visark.client.index.entity.NewTopicEntityProperty;
import com.podigua.visark.client.index.service.ConnectKafkaService;
import com.podigua.visark.client.utils.FormUtils;
import com.podigua.visark.core.utils.AlertUtils;
import com.podigua.visark.server.admin.TreeNode;
import com.podigua.visark.server.admin.entity.NewTopicEntity;
import com.podigua.visark.server.admin.service.AdminService;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.event.ClusterUpdateEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.*;


/**
 * @author: podigua
 * @create: 2021-09-06 17:22
 **/
@Service
public class IndexController implements Initializable, ApplicationListener<ClusterUpdateEvent> {
    public final static String ERROR = "error";
    public ToolBar toolBar;
    public HBox treePane;
    public TreeView<TreeNode> treeView;
    public HBox tabPane;
    public VBox root;
    public TabPane tab;
    private Cluster current;
    public ComboBox<Cluster> comboBox;
    private final ObservableList<Cluster> clusters = FXCollections.observableArrayList();
    private final FxmlService fxmlService;
    private final AdminService adminService;

    public IndexController(FxmlService fxmlService, AdminService adminService) {
        this.fxmlService = fxmlService;
        this.adminService = adminService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        treePane.prefHeightProperty().bind(root.heightProperty().subtract(toolBar.prefHeightProperty()));
        treeView.prefWidthProperty().bind(treePane.prefWidthProperty());
        treeView.prefHeightProperty().bind(treePane.prefHeightProperty());
        tabPane.prefWidthProperty().bind(root.widthProperty().subtract(treePane.prefWidthProperty()));
        tab.prefWidthProperty().bind(tabPane.prefWidthProperty());
        comboBox.setItems(clusters);
        ContextMenu menu = new ContextMenu();
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, current) -> {
            this.current = current;
            connectKafkaAndLoadTree(current, adminService);
        });
        treeView.setContextMenu(menu);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, current) -> {
            menu.getItems().clear();
            TreeNode node = current.getValue();
            switch (node.getType()) {
                case TOPIC:
                    MenuItem create = new MenuItem("新增");
                    create.setOnAction(event -> {
                        createTopic(current);
                    });
                    MenuItem refresh = new MenuItem("刷新");
                    refresh.setOnAction(event -> {
                        refreshTopic(current);
                    });
                    menu.getItems().addAll(create,refresh);
                    break;
                case TOPIC_INSTANCE:
                    MenuItem delete = new MenuItem("删除");
                    delete.setOnAction(event -> {
                        deleteTopic(node.getLabel(), current.getParent());
                    });
                    menu.getItems().addAll(delete);
                    break;
                default:
            }
        });
        treeView.setOnMouseClicked(event -> {
            int clickCount = event.getClickCount();
            MouseButton button = event.getButton();
            if (MouseButton.PRIMARY.equals(button) && clickCount == 2) {
                if (treeView.getSelectionModel().getSelectedItem() != null) {
                    TreeNode value = treeView.getSelectionModel().getSelectedItem().getValue();
                    System.out.println("value:   " + value);
                }
            }
        });
    }

    /**
     * 删除Topic
     *
     * @param topic
     * @param parent
     */
    private void deleteTopic(String topic, TreeItem<TreeNode> parent) {
        if (this.current != null) {
            AlertUtils.confirm("是否确认删除?").showAndWait().ifPresent(type -> {
                if (ButtonType.OK.equals(type)) {
                    Platform.runLater(() -> {
                        adminService.deleteTopic(current.getId(), topic);
                        refreshTopic(parent);
                    });
                }
            });
        }
    }

    /**
     * 创建 Topic
     *
     * @param node
     */
    private void createTopic(TreeItem<TreeNode> node) {
        NewTopicEntityProperty property = new NewTopicEntityProperty();
        Form form = Form.of(
                Group.of(
                        Field.ofStringType(property.getName()).label("名称").required("名称为必填项"),
                        Field.ofIntegerType(property.getPartitions()).label("分片数").required("分片为必填项"),
                        Field.ofDoubleType(property.getReplication()).label("副本数").required("副本数为必填项")
                ));
        Stage stage = new Stage();
        FormUtils.create(stage, form, 500, 350, event -> {
            if (!form.isValid()) {
                AlertUtils.alert("校验失败").showAndWait();
                return;
            }
            form.persist();
            NewTopicEntity entity = property.to();
            if (current != null) {
                adminService.createTopic(current.getId(), entity);
                refreshTopic(node);
            }
            stage.close();
        });
        stage.show();
    }

    /**
     * 刷新Topic
     *
     * @param node
     */
    private void refreshTopic(TreeItem<TreeNode> node) {
        if (current != null) {
            Platform.runLater(() -> {
                node.getChildren().clear();
                List<TreeNode> trees = adminService.getTopics(current.getId());
                TreeItem<TreeNode> topics = new TreeItem<>();
                buildTree(trees, topics);
                node.getChildren().addAll(topics.getChildren());
                treeView.refresh();
            });
        }
    }

    /**
     * 连接Kafka 加载数据
     *
     * @param current
     * @param adminService
     */
    private void connectKafkaAndLoadTree(Cluster current, AdminService adminService) {
        if (current == null) {
            return;
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(State.getInstance().getStage());
        stage.initStyle(StageStyle.UNIFIED);
        VBox root = new VBox();

        root.setAlignment(Pos.CENTER);
        ProgressBar progress = new ProgressBar();
        progress.prefWidthProperty().bind(root.widthProperty().multiply(0.8));
        Label label = new Label("连接中...");
        Button cancelButton = new Button("取消");
        Button closeButton = new Button("关闭");
        ConnectKafkaService service = new ConnectKafkaService(current.getId(), adminService);
        stage.setOnCloseRequest(event -> {
            if (service.isRunning()) {
                service.cancel();
                comboBox.setValue(null);
            }
        });
        closeButton.setOnAction(event -> {
            stage.close();
        });
        cancelButton.setOnAction(event -> {
            if (service.isRunning()) {
                service.cancel();
                comboBox.setValue(null);
            }
            stage.close();
        });
        root.getChildren().addAll(progress, label, cancelButton);
        stage.setScene(new Scene(root, 300, 100));
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.setFullScreen(false);
        stage.show();
        service.restart();
        service.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED.equals(newValue)) {
                label.setText("连接成功");
                progress.setProgress(100);
                stage.close();
                List<TreeNode> trees = service.getValue();
                TreeItem<TreeNode> rootItem = new TreeItem<>();
                buildTree(trees, rootItem);
                treeView.setShowRoot(false);
                treeView.setRoot(rootItem);
            }
        });
        service.messageProperty().addListener((observable, oldValue, newValue) -> {
            if (ERROR.equals(newValue)) {
                progress.setProgress(0);
                label.setText("连接失败");
                root.getChildren().remove(cancelButton);
                root.getChildren().add(closeButton);
                comboBox.setValue(null);
            }
        });
    }

    private void buildTree(List<TreeNode> trees, TreeItem<TreeNode> root) {
        List<TreeItem<TreeNode>> nodes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(trees)) {
            for (TreeNode tree : trees) {
                TreeItem<TreeNode> item = new TreeItem<>(tree);
                List<TreeNode> children = tree.getChildren();
                buildTree(children, item);
                nodes.add(item);
            }
        }
        root.getChildren().addAll(nodes);
    }


    @Override
    public void onApplicationEvent(ClusterUpdateEvent event) {
        List<Cluster> list = event.getCluster();
        this.clusters.clear();
        for (Cluster cluster : list) {
            this.clusters.add(cluster);
        }
    }

    /**
     * 集群配置
     *
     * @param event
     */
    public void cluster(ActionEvent event) {
        Stage stage = new Stage();
        stage.initOwner(State.getInstance().getStage());
        Parent parent = fxmlService.load("fxml/cluster/cluster.fxml");
        stage.setFullScreen(false);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.setScene(new Scene(parent));
        stage.show();
    }
}
