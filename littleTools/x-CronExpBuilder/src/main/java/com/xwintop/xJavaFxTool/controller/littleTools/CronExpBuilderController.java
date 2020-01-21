package com.xwintop.xJavaFxTool.controller.littleTools;

import cn.hutool.core.swing.clipboard.ClipboardUtil;
import com.xwintop.xJavaFxTool.services.littleTools.CronExpBuilderService;
import com.xwintop.xJavaFxTool.view.littleTools.CronExpBuilderView;
import com.xwintop.xcore.util.javafx.JavaFxViewUtil;
import com.xwintop.xcore.util.javafx.TooltipUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @ClassName: CronExpBuilderController
 * @Description: Cron表达式生成工具
 * @author: xufeng
 * @date: 2018/1/21 0021 1:08
 */

@Slf4j
@Getter
@Setter
public class CronExpBuilderController extends CronExpBuilderView {
    private CronExpBuilderService cronExpBuilderService = new CronExpBuilderService(this);
    private CheckBox[] secondCheckBox = new CheckBox[60];
    private CheckBox[] minuteCheckBox = new CheckBox[60];
    private CheckBox[] hourCheckBox = new CheckBox[24];
    private CheckBox[] dayCheckBox = new CheckBox[31];
    private CheckBox[] monthCheckBox = new CheckBox[12];
    private CheckBox[] weekCheckBox = new CheckBox[7];

    // private Tab[] tabs;
    private TextField[] cronTextFields;
    private String[] typeNameString = new String[]{"Second", "Minute", "Hour", "Day", "Month", "Week", "Year"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
    }

    private void initView() {
        // tabs = new Tab[] { tabSecond, tabMinute, tabHour, tabDay, tabMonth,
        // tabWeek, tabYear };
        cronTextFields = new TextField[]{jTF_Cron_Second, jTF_Cron_Minute, jTF_Cron_Hour, jTF_Cron_Day,
                jTF_Cron_Month, jTF_Cron_Week, jTF_Cron_Year};
        Calendar calendar = Calendar.getInstance();
        JavaFxViewUtil.setSpinnerValueFactory(secondStart_0, 1, 58);
        JavaFxViewUtil.setSpinnerValueFactory(secondEnd_0, 2, 59);
        JavaFxViewUtil.setSpinnerValueFactory(secondStart_1, 0, 59);
        JavaFxViewUtil.setSpinnerValueFactory(secondEnd_1, 1, 59);
        JavaFxViewUtil.setSpinnerValueFactory(minuteStart_0, 1, 58, calendar.get(Calendar.MINUTE));
        JavaFxViewUtil.setSpinnerValueFactory(minuteEnd_0, 2, 59, calendar.get(Calendar.MINUTE) + 1);
        JavaFxViewUtil.setSpinnerValueFactory(minuteStart_1, 0, 59, calendar.get(Calendar.MINUTE));
        JavaFxViewUtil.setSpinnerValueFactory(minuteEnd_1, 1, 59);
        JavaFxViewUtil.setSpinnerValueFactory(hourStart_0, 0, 23, calendar.get(Calendar.HOUR));
        JavaFxViewUtil.setSpinnerValueFactory(hourEnd_0, 2, 23, calendar.get(Calendar.HOUR) + 1);
        JavaFxViewUtil.setSpinnerValueFactory(hourStart_1, 0, 23, calendar.get(Calendar.HOUR));
        JavaFxViewUtil.setSpinnerValueFactory(hourEnd_1, 1, 23);
        JavaFxViewUtil.setSpinnerValueFactory(dayStart_0, 1, 31, calendar.get(Calendar.DAY_OF_MONTH));
        JavaFxViewUtil.setSpinnerValueFactory(dayEnd_0, 2, 31, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        JavaFxViewUtil.setSpinnerValueFactory(dayStart_1, 1, 31, calendar.get(Calendar.DAY_OF_MONTH));
        JavaFxViewUtil.setSpinnerValueFactory(dayEnd_1, 1, 31);
        JavaFxViewUtil.setSpinnerValueFactory(dayStart_2, 1, 31, calendar.get(Calendar.DAY_OF_MONTH));
        JavaFxViewUtil.setSpinnerValueFactory(monthStart_0, 1, 12, calendar.get(Calendar.MONTH) + 1);
        JavaFxViewUtil.setSpinnerValueFactory(monthEnd_0, 2, 12, calendar.get(Calendar.MONTH) + 1);
        JavaFxViewUtil.setSpinnerValueFactory(monthStart_1, 1, 12, calendar.get(Calendar.DAY_OF_MONTH));
        JavaFxViewUtil.setSpinnerValueFactory(monthEnd_1, 1, 12);
        JavaFxViewUtil.setSpinnerValueFactory(weekStart_0, 1, 7);
        JavaFxViewUtil.setSpinnerValueFactory(weekEnd_0, 2, 7);
        JavaFxViewUtil.setSpinnerValueFactory(weekStart_1, 1, 4);
        JavaFxViewUtil.setSpinnerValueFactory(weekEnd_1, 1, 7, calendar.get(Calendar.DAY_OF_WEEK));
        JavaFxViewUtil.setSpinnerValueFactory(weekStart_2, 1, 7);
        JavaFxViewUtil.setSpinnerValueFactory(yearStart_0, 2012, 3000, calendar.get(Calendar.YEAR));
        JavaFxViewUtil.setSpinnerValueFactory(yearEnd_0, 2013, 3000, calendar.get(Calendar.YEAR) + 1);
        for (int i = 0; i < 60; i++) {
            secondCheckBox[i] = new CheckBox(String.format("%02d", i));
            secondCheckBox[i].setLayoutX(32 + i % 10 * 60);
            secondCheckBox[i].setLayoutY(120 + i / 10 * 20);
            ((AnchorPane) tabSecond.getContent()).getChildren().add(secondCheckBox[i]);
            minuteCheckBox[i] = new CheckBox(String.format("%02d", i));
            minuteCheckBox[i].setLayoutX(32 + i % 10 * 60);
            minuteCheckBox[i].setLayoutY(120 + i / 10 * 20);
            ((AnchorPane) tabMinute.getContent()).getChildren().add(minuteCheckBox[i]);
            if (i < 24) {
                hourCheckBox[i] = new CheckBox(String.format("%02d", i));
                hourCheckBox[i].setLayoutX(32 + i % 10 * 60);
                hourCheckBox[i].setLayoutY(120 + i / 10 * 20);
                ((AnchorPane) tabHour.getContent()).getChildren().add(hourCheckBox[i]);
            }
            if (i < 31) {
                dayCheckBox[i] = new CheckBox(String.format("%2d", i + 1));
                dayCheckBox[i].setLayoutX(32 + i % 12 * 60);
                dayCheckBox[i].setLayoutY(200 + i / 12 * 20);
                ((AnchorPane) tabDay.getContent()).getChildren().add(dayCheckBox[i]);
            }
            if (i < 12) {
                monthCheckBox[i] = new CheckBox(String.format("%2d", i + 1));
                monthCheckBox[i].setLayoutX(32 + i % 12 * 50);
                monthCheckBox[i].setLayoutY(146);
                ((AnchorPane) tabMonth.getContent()).getChildren().add(monthCheckBox[i]);
            }
            if (i < 7) {
                weekCheckBox[i] = new CheckBox(String.format("%2d", i + 1));
                weekCheckBox[i].setLayoutX(32 + i % 10 * 60);
                weekCheckBox[i].setLayoutY(180 + i / 10 * 20);
                ((AnchorPane) tabWeek.getContent()).getChildren().add(weekCheckBox[i]);
            }
        }
    }

    private void initEvent() {
        for (int i = 0; i < cronTextFields.length; i++) {
            cronTextFields[i].textProperty().addListener(getChangeListener());
        }
        try {
            for (int i = 0; i < typeNameString.length - 1; i++) {
                addCheckBoxListener(typeNameString[i]);
                addRadioButtonListener(typeNameString[i]);
                addSpinnerListener(typeNameString[i]);
            }
            addRadioButtonYearListener();
            addSpinnerYearListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void addSpinnerYearListener() throws Exception {
        String checkType = "Year";
        ToggleGroup toggleGroup = (ToggleGroup) FieldUtils.readField(this, "toggleGroup" + checkType, true);
        String[] strings = new String[]{"Start_", "End_"};
        for (int i = 0; i < 2; i++) {
            final int ii = i;
            Spinner<Integer> spinnerStart = (Spinner<Integer>) FieldUtils.readField(this,
                    checkType.toLowerCase() + strings[i % 2] + (i / 2), true);
            spinnerStart.getEditor().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    toggleGroup.getToggles().get(ii / 2 + 1).setSelected(false);
                    toggleGroup.getToggles().get(ii / 2 + 1).setSelected(true);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    private void addSpinnerListener(String checkType) throws Exception {
        ToggleGroup toggleGroup = (ToggleGroup) FieldUtils.readField(this, "toggleGroup" + checkType, true);
        String[] strings = new String[]{"Start_", "End_"};
        for (int i = 0; i < 4; i++) {
            final int ii = i;
            Spinner<Integer> spinnerStart = (Spinner<Integer>) FieldUtils.readField(this,
                    checkType.toLowerCase() + strings[i % 2] + (i / 2), true);
            spinnerStart.getEditor().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    toggleGroup.getToggles().get(ii / 2 + 1).setSelected(false);
                    toggleGroup.getToggles().get(ii / 2 + 1).setSelected(true);
                }
            });
        }
        if ("Day".equals(checkType) || "Week".equals(checkType)) {
            Spinner<Integer> spinnerStart2 = (Spinner<Integer>) FieldUtils.readField(this,
                    checkType.toLowerCase() + "Start_2", true);
            spinnerStart2.getEditor().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    toggleGroup.getToggles().get(4).setSelected(false);
                    toggleGroup.getToggles().get(4).setSelected(true);
                }
            });
        }
    }

    // 单独添加年RadioButton监听事件
    private void addRadioButtonYearListener() throws Exception {
        String checkType = "Year";
        ToggleGroup toggleGroup = (ToggleGroup) FieldUtils.readField(this, "toggleGroup" + checkType, true);
        for (Toggle toggle : toggleGroup.getToggles()) {
            ((RadioButton) toggle).selectedProperty().addListener(new ChangeListener<Boolean>() {
                @SuppressWarnings("unchecked")
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue) {
                        return;
                    }
                    RadioButton radioButton = (RadioButton) ((BooleanProperty) observable).getBean();
                    try {
                        TextField textField = jTF_Cron_Year;
                        if (radioButton == toggleGroup.getToggles().get(0)) {
                            textField.setText("");
                        } else if (radioButton == toggleGroup.getToggles().get(1)) {
                            Spinner<Integer> spinnerStart = (Spinner<Integer>) FieldUtils.readField(
                                    CronExpBuilderController.this, checkType.toLowerCase() + "Start_0", true);
                            Spinner<Integer> spinnerEnd = (Spinner<Integer>) FieldUtils
                                    .readField(CronExpBuilderController.this, checkType.toLowerCase() + "End_0", true);
                            String string = spinnerStart.getValue() + "-" + spinnerEnd.getValue();
                            textField.setText(string);
                        } else if (radioButton == toggleGroup.getToggles().get(2)) {
                            textField.setText("*");
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    // 添加RadioButton监听事件
    private void addRadioButtonListener(String checkType) throws Exception {
        String checkTypeLowerCase = checkType.toLowerCase();
        ToggleGroup toggleGroup = (ToggleGroup) FieldUtils.readField(this, "toggleGroup" + checkType, true);
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @SuppressWarnings("unchecked")
            @Override
            public void changed(ObservableValue<? extends Toggle> var1, Toggle oldValue, Toggle newValue) {
                if (newValue == null) {
                    return;
                }
                RadioButton radioButton = (RadioButton) newValue;
                try {
                    TextField textField = (TextField) FieldUtils.readField(CronExpBuilderController.this,
                            "jTF_Cron_" + checkType, true);
                    // if(radioButton.getId().equals("radioButton" +
                    // checkType + "1")){
                    if (radioButton == toggleGroup.getToggles().get(0)) {
                        textField.setText("*");
                    } else if (radioButton == toggleGroup.getToggles().get(1)) {
                        Spinner<Integer> spinnerStart = (Spinner<Integer>) FieldUtils
                                .readField(CronExpBuilderController.this, checkTypeLowerCase + "Start_0", true);
                        Spinner<Integer> spinnerEnd = (Spinner<Integer>) FieldUtils
                                .readField(CronExpBuilderController.this, checkTypeLowerCase + "End_0", true);
                        String string = spinnerStart.getEditor().getText() + "-" + spinnerEnd.getEditor().getText();
                        textField.setText(string);
                    } else if (radioButton == toggleGroup.getToggles().get(2)) {
                        Spinner<Integer> spinnerStart = (Spinner<Integer>) FieldUtils
                                .readField(CronExpBuilderController.this, checkTypeLowerCase + "Start_1", true);
                        Spinner<Integer> spinnerEnd = (Spinner<Integer>) FieldUtils
                                .readField(CronExpBuilderController.this, checkTypeLowerCase + "End_1", true);
                        String string = spinnerStart.getEditor().getText() + "/" + spinnerEnd.getEditor().getText();
                        textField.setText(string);
                    } else if (radioButton == toggleGroup.getToggles().get(toggleGroup.getToggles().size() - 1)) {
                        CheckBox[] checkBoxs = (CheckBox[]) FieldUtils.readField(CronExpBuilderController.this,
                                checkTypeLowerCase + "CheckBox", true);
                        List<Integer> strList = new ArrayList<Integer>();
                        for (int i = 0; i < checkBoxs.length; i++) {
                            if (checkBoxs[i].isSelected()) {
                                strList.add(Integer.valueOf(checkBoxs[i].getText().trim()));
                            }
                        }
                        if (strList.isEmpty()) {
                            checkBoxs[0].setSelected(true);
                            textField.setText(Integer.valueOf(checkBoxs[0].getText().trim()).toString());
                        } else {
                            textField.setText(StringUtils.join(strList, ","));
                        }
                    } else if (radioButton == toggleGroup.getToggles().get(3)) {
                        if ("Day".equals(checkType) || "Month".equals(checkType) || "Week".equals(checkType)) {
                            textField.setText("?");
                        }
                    } else if (radioButton == toggleGroup.getToggles().get(4)) {
                        if ("Day".equals(checkType) || "Week".equals(checkType)) {
                            Spinner<Integer> spinnerStart = (Spinner<Integer>) FieldUtils
                                    .readField(CronExpBuilderController.this, checkTypeLowerCase + "Start_2", true);
                            if ("Day".equals(checkType)) {
                                textField.setText(spinnerStart.getEditor().getText() + "W");
                            } else if ("Week".equals(checkType)) {
                                textField.setText(spinnerStart.getEditor().getText() + "L");
                            }
                        }
                    } else if (radioButton == toggleGroup.getToggles().get(5)) {
                        if ("Day".equals(checkType)) {
                            textField.setText("L");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 添加指定CheckBox选中监听事件
    private void addCheckBoxListener(String checkType) throws Exception {
        CheckBox[] checkBoxs = (CheckBox[]) FieldUtils.readField(this, checkType.toLowerCase() + "CheckBox", true);
        for (int i = 0; i < checkBoxs.length; i++) {
            checkBoxs[i].selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    try {
                        List<Integer> strList = new ArrayList<Integer>();
                        for (int i = 0; i < checkBoxs.length; i++) {
                            if (checkBoxs[i].isSelected()) {
                                strList.add(Integer.valueOf(checkBoxs[i].getText().trim()));
                            }
                        }
                        TextField textField = (TextField) FieldUtils.readField(CronExpBuilderController.this,
                                "jTF_Cron_" + checkType, true);
                        ToggleGroup toggleGroup = (ToggleGroup) FieldUtils.readField(CronExpBuilderController.this,
                                "toggleGroup" + checkType, true);
                        if (strList.isEmpty()) {
                            textField.setText("*");
                            toggleGroup.selectToggle(toggleGroup.getToggles().get(0));
                        } else {
                            toggleGroup.selectToggle(toggleGroup.getToggles().get(toggleGroup.getToggles().size() - 1));
                            textField.setText(StringUtils.join(strList, ","));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private ChangeListener<String> getChangeListener() {
        return new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
                if (StringUtils.isEmpty(newValue)) {
                    return;
                }
                TextField textField = (TextField) ((StringProperty) arg0).getBean();
                // System.out.println(textField.getId() + "_" +
                // textField.getText() + "_" + oldValue + "_" + newValue);
                int currentIndex = mainTabPane.getSelectionModel().getSelectedIndex();
                for (int i = 0; i < cronTextFields.length; i++) {
                    if (textField == cronTextFields[i]) {
                        currentIndex = i;
                    }
                }
                // 当前选中项之前的如果为*，则都设置成0
                for (int i = currentIndex; i >= 1; i--) {
                    if (!"*".equals(cronTextFields[i].getText()) && "*".equals(cronTextFields[i - 1].getText())) {
                        if (i < 3) {
                            cronTextFields[i - 1].setText("0");
                        } else {
                            cronTextFields[i - 1].setText("1");
                        }
                    }
                }
                // 当前选中项之后的如果不为*则都设置成*
                if ("*".equals(cronTextFields[currentIndex].getText().trim())) {
                    for (int i = currentIndex + 1; i < cronTextFields.length; i++) {
                        if (i == 5) {
                            cronTextFields[i].setText("?");
                        } else if (i == 6) {
                            cronTextFields[i].setText(null);
                        } else {
                            cronTextFields[i].setText("*");
                        }
                    }
                }
                List<String> sList = new ArrayList<String>();
                for (int i = 0; i < cronTextFields.length; i++) {
                    if (StringUtils.isNotEmpty(cronTextFields[i].getText())) {
                        sList.add(cronTextFields[i].getText().trim());
                    }
                }
                jTF_Cron_Exp.setText(StringUtils.join(sList, " "));
                jTF_Cron_ExpCronTab.setText(CronExpBuilderService.getCronToCronTab(jTF_Cron_Exp.getText()));
                cronExpBuilderService.runCronExpressionAction();
            }
        };
    }

    @FXML
    private void parseActionPerformed(ActionEvent event) throws Exception {
        try {
            cronExpBuilderService.parseActionPerformed();
        } catch (Exception e) {
            jTA_Schedule_Next.setText(e.getMessage());
            TooltipUtil.showToast("反解析表达式异常！" + e.getMessage());
        }
    }

    @FXML
    private void copyExpAction(ActionEvent event) throws Exception {
        ClipboardUtil.setStr(jTF_Cron_Exp.getText());
        TooltipUtil.showToast("复制成功！");
    }

    @FXML
    private void parseActionPerformedCronTab(ActionEvent event) throws Exception {
        try {
            cronExpBuilderService.parseActionPerformedCronTab();
        } catch (Exception e) {
            jTA_Schedule_Next.setText(e.getMessage());
            TooltipUtil.showToast("反解析表达式异常！" + e.getMessage());
        }
    }

    @FXML
    private void copyExpActionCronTab(ActionEvent event) throws Exception {
        ClipboardUtil.setStr(jTF_Cron_ExpCronTab.getText());
        TooltipUtil.showToast("复制成功！");
    }

    @FXML
    private void runCronExpressionAction(ActionEvent event) throws Exception {
        cronExpBuilderService.runCronExpressionAction();
    }

    @FXML
    private void cronExplainAction(ActionEvent event) throws Exception {
        String url = "/com/xwintop/xJavaFxTool/web/littleTools/cron表达式详解.html";
        JavaFxViewUtil.openUrlOnWebView(url, "cron表达式详解", null);
    }
}