package com.podigua.visark.client.index.controller;

import com.podigua.javafx.State;
import com.podigua.javafx.support.FxmlService;
import com.podigua.visark.server.admin.TreeNode;
import com.podigua.visark.server.admin.service.AdminService;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.event.ClusterUpdateEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author: podigua
 * @create: 2021-09-06 17:22
 **/
@Service
public class IndexController implements Initializable, ApplicationListener<ClusterUpdateEvent> {
    public ToolBar toolBar;
    public HBox treePane;
    public TreeView<TreeNode> treeView;
    public HBox tabPane;
    public VBox root;
    public TabPane tab;
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
            List<TreeNode> trees = adminService.getTrees(current.getId());
            TreeItem<TreeNode> root = new TreeItem<>();
            buildTree(trees, root);
            treeView.setShowRoot(false);
            treeView.setRoot(root);
        });
        treeView.setContextMenu(menu);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, current) -> {
            menu.getItems().clear();
            TreeNode node = current.getValue();
            switch (node.getType()) {
                case TOPIC:
                    menu.getItems().add(new MenuItem("新增"));
                    break;
                case TOPIC_INSTANCE:
                    menu.getItems().add(new MenuItem("新增"));
                    menu.getItems().add(new MenuItem("删除"));
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
