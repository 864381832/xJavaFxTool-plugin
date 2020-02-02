package com.xwintop.xJavaFxTool.services.developTools.xTransferTool;

import com.jfoenix.controls.JFXCheckBox;
import com.xwintop.xJavaFxTool.controller.developTools.xTransferTool.TransferToolServiceViewController;
import com.xwintop.xTransfer.receiver.bean.ReceiverConfigFtp;
import com.xwintop.xTransfer.receiver.bean.ReceiverConfigSftp;
import com.xwintop.xcore.util.javafx.JavaFxViewUtil;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Slf4j
public class TransferToolServiceViewService {
    private TransferToolServiceViewController transferToolServiceViewController;

    private Map<String, Tab> rowTabMap = new HashMap<>();

    private Object configObject;

    public TransferToolServiceViewService(TransferToolServiceViewController transferToolServiceViewController) {
        this.transferToolServiceViewController = transferToolServiceViewController;
    }

    public void saveConfigAction(Object configObject, FlowPane flowPane) {
        for (Field field : configObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            for (Node node : flowPane.getChildren()) {
                if (field.getName().equals(node.getId())) {
                    try {
                        if (field.getType() == String.class) {
                            if ("strategy".equals(field.getName())) {
                                field.set(configObject, ((ChoiceBox<String>) node).getValue());
                            } else {
                                if (StringUtils.isBlank(((TextField) node).getText())) {
                                    field.set(configObject, null);
                                } else {
                                    field.set(configObject, ((TextField) node).getText());
                                }
                            }
                        } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                            field.set(configObject, ((CheckBox) node).isSelected());
                        } else if (field.getType() == int.class) {
                            if ("connectionType".equals(field.getName())) {
                                field.set(configObject, ((ChoiceBox<String>) node).getSelectionModel().getSelectedIndex());
                            } else {
                                if (((Spinner<Integer>) node).getValue() != null) {
                                    field.set(configObject, ((Spinner<Integer>) node).getValue());
                                }
                            }
                        } else if (field.getType() == long.class) {
                            if (((Spinner<Double>) node).getValue() != null) {
                                field.set(configObject, ((Spinner<Double>) node).getValue().longValue());
                            }
                        } else if (field.getType() == Map.class) {
                            TableView<Map<String, String>> propertiesTableView = (TableView<Map<String, String>>) node;
                            Map<String, String> propertiesMap = new HashMap<>();
                            for (Map<String, String> map : propertiesTableView.getItems()) {
                                propertiesMap.put(map.get("key"), map.get("value"));
                            }
                            field.set(configObject, propertiesMap);
                        } else if (field.getType() == List.class) {
                            if (configObject instanceof ReceiverConfigFtp || configObject instanceof ReceiverConfigSftp) {

                            } else {
                                List<String> list = new ArrayList<>(((ListView<String>) node).getItems());
                                field.set(configObject, list);
                            }
                        } else if (field.getType() == Integer.class) {
                            if (StringUtils.isBlank(((TextField) node).getText())) {
                                field.set(configObject, null);
                            } else {
                                field.set(configObject, Integer.valueOf(((TextField) node).getText()));
                            }
                        } else if (field.getType() == Long.class) {
                            if (StringUtils.isBlank(((TextField) node).getText())) {
                                field.set(configObject, null);
                            } else {
                                field.set(configObject, Long.valueOf(((TextField) node).getText()));
                            }
                        } else {
                            if (StringUtils.isBlank(((TextField) node).getText())) {
                                field.set(configObject, null);
                            } else {
                                field.set(configObject, ((TextField) node).getText());
                            }
                        }
                    } catch (Exception e) {
                        log.error("保存失败：", e);
                    }
                    break;
                }
            }
        }
    }

    public void addRowTabPane(Object rowConfigObject, int selectIndex) {
        String tabName = "row" + selectIndex;
        Tab tab1 = rowTabMap.get(tabName);
        if (tab1 != null) {
            transferToolServiceViewController.getRowTabPane().getSelectionModel().select(tab1);
            return;
        }
        final Tab tab = new Tab(tabName);
        tab.setOnClosed(event -> {
            transferToolServiceViewController.getRowTabPane().getTabs().remove(rowTabMap.get(tabName));
            rowTabMap.remove(tab.getText());
        });
        transferToolServiceViewController.getRowTabPane().getTabs().add(tab);
        transferToolServiceViewController.getRowTabPane().getSelectionModel().select(tab);
        rowTabMap.put(tabName, tab);
        this.addRowFlowPane(tab, rowConfigObject);
    }

    public void addRowFlowPane(Tab tab, Object rowConfigObject) {
        FlowPane rowFlowPane = new FlowPane();
        rowFlowPane.setPadding(new Insets(5, 5, 5, 5));
        rowFlowPane.setHgap(20);
        rowFlowPane.setVgap(10);
//        Button saveButton = new Button("保存");
//        saveButton.setOnAction(event -> {
//            saveConfigAction(rowConfigObject, rowFlowPane);
//        });
//        rowFlowPane.getChildren().add(saveButton);
        this.addFlowPane(rowConfigObject, rowFlowPane);
        tab.setContent(rowFlowPane);
    }

    public void addFlowPane(Object configObject, FlowPane flowPane) {
        for (Field field : configObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.getType() == String.class) {
                    Label label = new Label(field.getName() + ":");
                    label.setTextFill(Color.RED);
                    FlowPane.setMargin(label, new Insets(0, -15, 0, 0));
                    flowPane.getChildren().add(label);
                    if ("strategy".equals(field.getName())) {
                        ChoiceBox<String> choiceBox = new ChoiceBox();
                        choiceBox.setId(field.getName());
                        String[] strategyChoiceBoxStrings = new String[]{"direct", "day", "hour", "day_hour", "minutes"};
                        choiceBox.getItems().addAll(strategyChoiceBoxStrings);
                        choiceBox.setValue(field.get(configObject) == null ? "direct" : field.get(configObject).toString());
                        flowPane.getChildren().addAll(choiceBox);
                        choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                field.set(configObject, newValue);
                            } catch (IllegalAccessException e) {
                                log.error("设置值失败：", e);
                            }
                        });
                    } else {
                        TextField textField = new TextField(field.get(configObject) == null ? "" : field.get(configObject).toString());
                        textField.setMinWidth(30);
                        textField.setMaxWidth(400);
                        textField.prefColumnCountProperty().bind(textField.textProperty().length());
                        textField.setId(field.getName());
                        if ("serviceName".equals(field.getName())) {
                            textField.setEditable(false);
                            textField.setMaxWidth(150);
                        }
                        flowPane.getChildren().add(textField);
                        textField.textProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                if (StringUtils.isBlank(newValue)) {
                                    field.set(configObject, null);
                                } else {
                                    field.set(configObject, newValue);
                                }
                            } catch (IllegalAccessException e) {
                                log.error("设置值失败：", e);
                            }
                        });
                    }
                } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                    CheckBox checkBox = new CheckBox(field.getName());
                    checkBox.setId(field.getName());
                    checkBox.setSelected(field.getBoolean(configObject));
                    flowPane.getChildren().add(checkBox);
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        try {
                            field.set(configObject, newValue);
                        } catch (IllegalAccessException e) {
                            log.error("设置值失败：", e);
                        }
                    });
                } else if (field.getType() == int.class || field.getType() == long.class) {
                    Label label = new Label(field.getName() + ":");
                    label.setTextFill(Color.RED);
                    FlowPane.setMargin(label, new Insets(0, -15, 0, 0));
                    flowPane.getChildren().add(label);
                    if ("connectionType".equals(field.getName())) {
                        ChoiceBox<String> choiceBox = new ChoiceBox();
                        choiceBox.setId(field.getName());
                        String[] choiceBoxStrings = new String[]{"0:FTP", "1:FTP using implicit SSL", "2:FTP using explicit SSL(Auth SSL)", "3:FTP using explicit SSL(Auth TLS)"};
                        choiceBox.getItems().addAll(choiceBoxStrings);
                        choiceBox.getSelectionModel().select((int) field.get(configObject));
                        flowPane.getChildren().addAll(choiceBox);
                        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                field.set(configObject, newValue);
                            } catch (IllegalAccessException e) {
                                log.error("设置值失败：", e);
                            }
                        });
                    } else {
                        Spinner spinner = null;
                        if (field.getType() == int.class) {
                            spinner = new Spinner<Integer>();
                            JavaFxViewUtil.setSpinnerValueFactory((Spinner<Integer>) spinner, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.valueOf(field.get(configObject).toString()));
                            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                                try {
                                    field.set(configObject, newValue);
                                } catch (IllegalAccessException e) {
                                    log.error("设置值失败：", e);
                                }
                            });
                        } else {
                            spinner = new Spinner<Double>();
                            JavaFxViewUtil.setSpinnerValueFactory(spinner, (double) Long.MIN_VALUE, (double) Long.MAX_VALUE, (double) Long.valueOf(field.get(configObject).toString()));
                            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                                try {
                                    field.set(configObject, ((Double) newValue).longValue());
                                } catch (IllegalAccessException e) {
                                    log.error("设置值失败：", e);
                                }
                            });
                        }
                        spinner.setId(field.getName());
                        spinner.setEditable(true);
                        flowPane.getChildren().add(spinner);
                        spinner.setMinWidth(70);
                        spinner.setMaxWidth(400);
                        spinner.getEditor().prefColumnCountProperty().bind(spinner.getEditor().textProperty().length());
                    }
                } else if (field.getType() == Map.class) {
                    Label label = new Label(field.getName() + ":");
                    label.setTextFill(Color.RED);
                    FlowPane.setMargin(label, new Insets(0, -15, 0, 0));
                    flowPane.getChildren().add(label);
                    TableView<Map<String, String>> propertiesTableView = new TableView<>();
                    propertiesTableView.setId(field.getName());
                    propertiesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    propertiesTableView.setPrefHeight(80);
                    Runnable runnable = () -> {
                        Map<String, String> propertiesMap = new HashMap<>();
                        for (Map<String, String> map : propertiesTableView.getItems()) {
                            propertiesMap.put(map.get("key"), map.get("value"));
                        }
                        try {
                            field.set(configObject, propertiesMap);
                        } catch (IllegalAccessException e) {
                            log.error("设置值失败：", e);
                        }
                    };
                    TableColumn<Map<String, String>, String> propertiesKeyTableColumn = new TableColumn<>("key");
                    TableColumn<Map<String, String>, String> propertiesValueTableColumn = new TableColumn<>("value");
                    propertiesTableView.getColumns().add(propertiesKeyTableColumn);
                    propertiesTableView.getColumns().add(propertiesValueTableColumn);
                    JavaFxViewUtil.setTableColumnMapValueFactory(propertiesKeyTableColumn, "key", true, runnable);
                    JavaFxViewUtil.setTableColumnMapValueFactory(propertiesValueTableColumn, "value", true, runnable);
                    ObservableList<Map<String, String>> propertiesTableData = FXCollections.observableArrayList();
                    propertiesTableView.setItems(propertiesTableData);
                    ((Map) field.get(configObject)).forEach((key, value) -> {
                        Map<String, String> map = new HashMap<>();
                        map.put("key", String.valueOf(key));
                        map.put("value", String.valueOf(value));
                        propertiesTableData.add(map);
                    });
                    JavaFxViewUtil.addTableViewOnMouseRightClickMenu(propertiesTableView);
                    flowPane.getChildren().add(propertiesTableView);
                } else if (field.getType() == List.class) {
                    Label label = new Label(field.getName() + ":");
                    label.setTextFill(Color.RED);
                    FlowPane.setMargin(label, new Insets(0, -15, 0, 0));
                    flowPane.getChildren().add(label);
                    ListView<String> listView = new ListView<>();
                    listView.setId(field.getName());
                    listView.setPrefHeight(80);
                    ObservableList<String> listData = FXCollections.observableArrayList();
                    listView.setItems(listData);
                    if (configObject instanceof ReceiverConfigFtp || configObject instanceof ReceiverConfigSftp) {
                        this.addRowPane(configObject, (List) field.get(configObject), listView);
                    } else {
                        listData.addAll((List) field.get(configObject));
                        JavaFxViewUtil.addListViewOnMouseRightClickMenu(listView);
                        listData.addListener(new ListChangeListener<String>() {
                            @Override
                            public void onChanged(Change<? extends String> c) {
                                List<String> list = new ArrayList<>(listData);
                                try {
                                    field.set(configObject, list);
                                } catch (IllegalAccessException e) {
                                    log.error("设置值失败：", e);
                                }
                            }
                        });
                    }
                    flowPane.getChildren().add(listView);
                } else {
                    Label label = new Label(field.getName() + ":");
                    label.setTextFill(Color.RED);
                    FlowPane.setMargin(label, new Insets(0, -15, 0, 0));
                    flowPane.getChildren().add(label);
                    TextField textField = new TextField(field.get(configObject) == null ? "" : field.get(configObject).toString());
                    textField.setId(field.getName());
                    textField.setMinWidth(30);
                    textField.setMaxWidth(400);
                    textField.prefColumnCountProperty().bind(textField.textProperty().length());
                    flowPane.getChildren().add(textField);
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        try {
                            if (StringUtils.isBlank(newValue)) {
                                field.set(configObject, null);
                            } else {
                                if (field.getType() == Integer.class) {
                                    field.set(configObject, Integer.valueOf(newValue));
                                } else if (field.getType() == Long.class) {
                                    field.set(configObject, Long.valueOf(newValue));
                                } else {
                                    field.set(configObject, newValue);
                                }
                            }
                        } catch (IllegalAccessException e) {
                            log.error("设置值失败：", e);
                        }
                    });
                }
            } catch (IllegalAccessException e) {
                log.error("加载失败", e);
            }
        }
    }

    public void addRowPane(Object configObject, List rowList, ListView<String> listView) {
        rowList.forEach(o -> {
            if (o instanceof ReceiverConfigFtp.ReceiverConfigFtpRow) {
                listView.getItems().add("ReceiverConfigFtpRow");
            } else if (o instanceof ReceiverConfigSftp.ReceiverConfigSftpRow) {
                listView.getItems().add("ReceiverConfigSftpRow");
            }
        });
        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                MenuItem menuAdd = new MenuItem("添加行");
                menuAdd.setOnAction(event1 -> {
                    if (configObject instanceof ReceiverConfigFtp) {
                        listView.getItems().add("ReceiverConfigFtpRow");
                        ReceiverConfigFtp.ReceiverConfigFtpRow receiverConfigFtpRow = new ReceiverConfigFtp.ReceiverConfigFtpRow();
                        rowList.add(receiverConfigFtpRow);
                    } else if (configObject instanceof ReceiverConfigSftp) {
                        listView.getItems().add("ReceiverConfigSftpRow");
                        ReceiverConfigSftp.ReceiverConfigSftpRow receiverConfigSftpRow = new ReceiverConfigSftp.ReceiverConfigSftpRow();
                        rowList.add(receiverConfigSftpRow);
                    }
                });
                MenuItem menu_Copy = new MenuItem("复制选中行");
                menu_Copy.setOnAction(event1 -> {
                    try {
                        rowList.add(listView.getSelectionModel().getSelectedIndex(), BeanUtils.cloneBean(rowList.get(listView.getSelectionModel().getSelectedIndex())));
                    } catch (Exception e) {
                        log.error("复制失败", e);
                    }
                    listView.getItems().add(listView.getSelectionModel().getSelectedIndex(), listView.getSelectionModel().getSelectedItem());
                });
                MenuItem menu_Remove = new MenuItem("删除选中行");
                menu_Remove.setOnAction(event1 -> {
                    rowList.remove(listView.getSelectionModel().getSelectedIndex());
                    listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
                });
                MenuItem menu_RemoveAll = new MenuItem("删除所有");
                menu_RemoveAll.setOnAction(event1 -> {
                    listView.getItems().clear();
                    rowList.clear();
                });
                listView.setContextMenu(new ContextMenu(menuAdd, menu_Copy, menu_Remove, menu_RemoveAll));
            } else if (event.getButton() == MouseButton.PRIMARY) {
                int selectIndex = listView.getSelectionModel().getSelectedIndex();
                if (listView.getSelectionModel().getSelectedItems() == null || selectIndex == -1) {
                    return;
                }
                this.addRowTabPane(rowList.get(selectIndex), selectIndex);
//                transferToolServiceViewController.getServiceViewSplitPane().setDividerPositions(0.5);
            }
        });
    }
}