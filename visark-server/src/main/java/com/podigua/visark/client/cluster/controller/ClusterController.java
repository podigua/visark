package com.podigua.visark.client.cluster.controller;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.podigua.visark.client.cluster.entity.ClusterProperty;
import com.podigua.visark.client.utils.FormUtils;
import com.podigua.visark.core.utils.AlertUtils;
import com.podigua.visark.core.utils.ImageUtils;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.event.ClusterUpdateEvent;
import com.podigua.visark.server.cluster.service.ClusterService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author: podigua
 * @create: 2021-09-07 10:00
 **/
@Service
public class ClusterController implements Initializable, ApplicationListener<ClusterUpdateEvent> {
    public TableView<Cluster> tableView;
    private ObservableList<Cluster> clusters = FXCollections.observableArrayList();
    public ToolBar toolBar;
    public VBox pane;
    @Autowired
    private ClusterService clusterService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.prefHeightProperty().bind(pane.heightProperty().subtract(toolBar.prefHeightProperty()));
        TableColumn<Cluster, String> nameColumn = new TableColumn("名称");
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        TableColumn<Cluster, String> descriptionColumn = new TableColumn("描述");
        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<>("description")
        );
        TableColumn<Cluster, String> actionColumn = new TableColumn("操作");
        nameColumn.setPrefWidth(200);
        actionColumn.setPrefWidth(80);
        descriptionColumn.prefWidthProperty().bind(tableView.widthProperty().subtract(nameColumn.prefWidthProperty()).subtract(actionColumn.prefWidthProperty()).subtract(10));
        actionColumn.setCellFactory(col -> {
            TableCell<Cluster, String> cell = new TableCell<Cluster, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    HBox graphic = new HBox();
                    Button edit = getEditButton();
                    Button del = getDeleteButton();
                    graphic.getChildren().addAll(edit, del);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        this.setGraphic(graphic);
                    }
                }

                private Button getEditButton() {
                    Button button = new Button();
                    button.setGraphic(ImageUtils.load("images/edit.png", 16, 16));
                    button.setOnAction((col) -> {
                        Cluster cluster = clusters.get(getIndex());
                        edit(ClusterProperty.from(cluster));
                    });
                    return button;
                }

                private Button getDeleteButton() {
                    Button button = new Button();
                    button.setGraphic(ImageUtils.load("images/delete.png", 16, 16));
                    button.setOnAction((col) -> {
                        Alert alert = new Alert(AlertType.NONE, "是否确认删除?", ButtonType.CANCEL, ButtonType.OK);
                        Optional<ButtonType> buttonType = alert.showAndWait();
                        if (ButtonType.OK.equals(buttonType.get())) {
                            Cluster cluster = clusters.get(getIndex());
                            clusterService.deleteById(cluster.getId());
                        }
                    });
                    return button;
                }


            };
            return cell;
        });
        tableView.getColumns().addAll(nameColumn, descriptionColumn, actionColumn);
        tableView.setItems(clusters);
    }

    /**
     * 新增集群
     *
     * @param event
     */
    public void add(ActionEvent event) {
        ClusterProperty property = new ClusterProperty();
        edit(property);
    }

    private void edit(ClusterProperty property) {
        Form form = Form.of(Group.of(Field.ofStringType(property.getName())
                        .label("名称")
                        .required("名称为必填项")
                , Field.ofStringType(property.getBootstrapServers())
                        .label("地址")
                        .required("地址为必填项")
                , Field.ofStringType(property.getDescription())
                        .label("描述").multiline(true))
        ).title("集群");
        Stage stage = new Stage();
        FormUtils.create(stage, form, 500, 280, e -> {
            if (!form.isValid()) {
                AlertUtils.alert("校验失败").showAndWait();
                return;
            }
            form.persist();
            clusterService.save(property.to());
            AlertUtils.alert("保存成功").showAndWait();
            stage.close();
        });
        stage.show();
    }

    @Override
    public void onApplicationEvent(ClusterUpdateEvent event) {
        List<Cluster> list = event.getCluster();
        this.clusters.clear();
        this.clusters.addAll(list);
    }
}
