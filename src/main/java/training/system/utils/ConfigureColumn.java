package training.system.utils;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import training.system.core.domain.user.User;

import java.util.Date;

public class ConfigureColumn {
    public static <T> void configureTextColumn(TableColumn<T, String> column) {
        column.setStyle("-fx-alignment: CENTER;");
        column.setMinWidth(150);
        column.setMaxWidth(250);

        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    setTooltip(null);
                } else if (item == null) {
                    setText("-");
                } else {
                    Tooltip tooltip = new Tooltip();
                    setText(item.length() > 80 ? item.substring(0, 80) + "..." : item);

                    tooltip.setText(item);
                    tooltip.setMaxWidth(300);
                    tooltip.setWrapText(true);
                    setTooltip(tooltip);
                }
            }
        });
    }

    public static <T> void configureTrainerColumn(TableColumn<T, User> column) {
        column.setStyle("-fx-alignment: CENTER;");
        column.setMinWidth(100);
        column.setMaxWidth(250);

        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(User trainer, boolean empty) {
                super.updateItem(trainer, empty);
                if (empty) {
                    setText(null);
                } else if (trainer == null) {
                    setText("-");
                } else {
                    setText(trainer.getName());
                    setTooltip(new Tooltip(trainer.getName()));
                }
            }
        });
    }

    public static <T> void configureBooleanColumn(TableColumn<T, Boolean> column) {
        column.setStyle("-fx-alignment: CENTER;");
        column.setMinWidth(100);
        column.setMaxWidth(250);

        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Sí" : "No");
                    setTooltip(new Tooltip(item ? "Sí" : "No"));
                }
            }
        });
    }

    public static <T> void configureDateColumn(TableColumn<T, Date> column) {
        column.setStyle("-fx-alignment: CENTER;");
        column.setMinWidth(100);
        column.setMaxWidth(250);

        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText("-");
                } else {
                    setText(item.toString());
                    setTooltip(new Tooltip(item.toString()));
                }
            }
        });
    }

    public static <T> void configureIntegerColumn(TableColumn<T, Integer> column) {
        column.setStyle("-fx-alignment: CENTER;");
        column.setMinWidth(100);
        column.setMaxWidth(250);

        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText("-");
                } else {
                    setText(item.toString());
                    setTooltip(new Tooltip(item.toString()));
                }
            }
        });
    }
}
