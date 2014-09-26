package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientFX extends Application{

    private Client client;
    
    //Подгружаем иконки для кнопок
    private static final Image ICON_ADD = new Image(ClientFX.class.getResourceAsStream("add.png"));
    private static final Image ICON_DELETE = new Image(ClientFX.class.getResourceAsStream("delete.png"));
    private static final Image ICON_FIND = new Image(ClientFX.class.getResourceAsStream("find.png"));
    private static final Image ICON_EDIT = new Image(ClientFX.class.getResourceAsStream("edit.png"));
    private static final Image ICON_REFRESH = new Image(ClientFX.class.getResourceAsStream("refresh.png"));

    //То, что будет выполнено при старте приложения
    @Override public void start(Stage primaryStage) throws Exception {
        try{
            client = new Client();
            //Делаем из них объект ImageView
            ImageView addImageView = new ImageView(ICON_ADD);
            ImageView deleteImageView = new ImageView(ICON_DELETE);
            ImageView findImageView = new ImageView(ICON_FIND);
            ImageView editImageView = new ImageView(ICON_EDIT);
            ImageView refreshImageView = new ImageView(ICON_REFRESH);

            client.connect(client);
            client.Update();
            
            //Создаем окошко
            Scene scene = new Scene(new Group());

            //Создаем таблицу
            TableColumn idCol = new TableColumn();
            idCol.setText("Id");
            idCol.setMinWidth(25);
            idCol.setCellValueFactory(new PropertyValueFactory("id"));

            TableColumn nameCol = new TableColumn();
            nameCol.setText("Name");
            nameCol.setMinWidth(100);
            nameCol.setCellValueFactory(new PropertyValueFactory("name"));

            TableColumn publisherCol = new TableColumn();
            publisherCol.setMinWidth(100);
            publisherCol.setText("Publisher");
            publisherCol.setCellValueFactory(new PropertyValueFactory("publisher"));

            TableColumn dateCol = new TableColumn();
            dateCol.setText("Date");
            dateCol.setMinWidth(45);
            dateCol.setCellValueFactory(new PropertyValueFactory("date"));

            TableColumn pagesCol = new TableColumn();
            pagesCol.setText("Pages");
            pagesCol.setMinWidth(50);
            pagesCol.setCellValueFactory(new PropertyValueFactory("pages"));

            TableColumn smoothCol = new TableColumn();
            smoothCol.setText("Smooth");
            smoothCol.setMinWidth(70);
            smoothCol.setCellValueFactory(new PropertyValueFactory("smooth"));

            final TableView tableView = new TableView();
            tableView.setMaxWidth(395);
            tableView.setItems(client.data);
            tableView.getColumns().addAll(idCol, nameCol, publisherCol, dateCol, pagesCol, smoothCol);

            //Создаем кнопки
            HBox hBox = new HBox();
            hBox.setSpacing(5);

            //Кнопака добавления элемента
            Button addButton = new Button("", addImageView);
            addButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent event) {
                    try{
                        final Stage dialog = new Stage();

                        VBox box = new VBox();
                        box.setAlignment(Pos.CENTER);

                        //Стоблец с полями для ввода
                        VBox vBox = new VBox(); 
                        final TextField id = new TextField();
                        id.setPromptText("Enter id");
                        final TextField name = new TextField();
                        name.setPromptText("Enter name");
                        final TextField publisher = new TextField();
                        publisher.setPromptText("Enter publisher");
                        final TextField date = new TextField();
                        date.setPromptText("Enter date");
                        final TextField pages = new TextField();
                        pages.setPromptText("Enter number of pages");
                        final TextField smooth = new TextField();
                        smooth.setPromptText("Is it smooth?");
                        vBox.getChildren().addAll(id, name, publisher, date, pages, smooth);

                        HBox buttons = new HBox();
                        buttons.setAlignment(Pos.CENTER);
                        Button addDialogButton = new Button("Add");
                        addDialogButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent event) {
                                try {
                                    Book b = new Book(Integer.parseInt(id.getText()),name.getText(),publisher.getText(), Integer.parseInt(date.getText()), Integer.parseInt(pages.getText()), Boolean.parseBoolean(smooth.getText()));
                                    id.clear();
                                    name.clear();
                                    publisher.clear();
                                    date.clear();
                                    pages.clear();
                                    smooth.clear();
                                    client.addData(b);
                                }catch (NumberFormatException e){
                                    
                                    
                                    final Stage error = new Stage();
                                    VBox box = new VBox();
                                    box.setAlignment(Pos.CENTER);
                                    final Label date = new Label("Incorrect data! Try again!");
                                    Button okDialogButton = new Button("Ok");
                                    okDialogButton.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent event) {
                                            error.close();
                                        }
                                     });
                                    box.getChildren().addAll(date, okDialogButton);
                                    Scene scene1 = new Scene(box);
                                    error.setScene(scene1);
                                    error.show();
                                    
//                                    
//                                    System.err.println("NaN! Try again!");
                                }
                                dialog.close();
                            }
                        });
                        
                        Button cancelDialogButton = new Button("Cancel");
                        cancelDialogButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent event) {
                                dialog.close();
                            }
                        });
                        buttons.getChildren().addAll(addDialogButton, cancelDialogButton);

                        box.getChildren().addAll(new Label("Add a note"), vBox, buttons);
                        Scene scene1 = new Scene(box);
                        dialog.setScene(scene1);
                        dialog.show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            hBox.getChildren().add(addButton);

            //Кнопка удаления
            Button deleteButton = new Button("", deleteImageView);
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent event) {
                    try{
                        final int index;
                            ObservableList<TablePosition> data2 = FXCollections.observableArrayList();
                            data2 = tableView.getSelectionModel().getSelectedCells();
                            index = data2.get(0).getRow();
                            
                            client.deleteDate(client.data.get(index).getId());

                    }catch(Exception e){
                        final Stage error = new Stage();
                                    VBox box = new VBox();
                                    box.setAlignment(Pos.CENTER);
                                    final Label date = new Label("At first, select line which you want to delete");
                                    Button okDialogButton = new Button("Ok");
                                    okDialogButton.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent event) {
                                            error.close();
                                        }
                                     });
                                    box.getChildren().addAll(date, okDialogButton);
                                    Scene scene1 = new Scene(box);
                                    error.setScene(scene1);
                                    error.show();
                    }
                }
            });
            hBox.getChildren().add(deleteButton);

            //Кнопка поиска
            Button findButton = new Button("", findImageView);
            findButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent event) {
                    try{
                        final Stage dialog = new Stage();

                        VBox box = new VBox();
                        box.setAlignment(Pos.CENTER);

                        HBox hBox = new HBox(); 
                        final TextField number = new TextField();

                        number.setPromptText("What you wanna find?");
                        final ChoiceBox cb = new ChoiceBox();
                        cb.getItems().addAll("Name", "Publisher", "Date", "Pages");
                        cb.getSelectionModel().selectFirst();
                        hBox.getChildren().addAll(number, cb);

                        HBox buttons = new HBox();
                        buttons.setAlignment(Pos.CENTER);
                        Button okDialogButton = new Button("Ok");
                        okDialogButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent event) {
                                try {
                                    client.data.clear();
                                    client.data.addAll(client.search(cb.getValue().toString(), number.getText()));
                                    number.clear();
                                }catch (NumberFormatException e){
                                    System.err.println("NaN! Try again!");
                                }
                                dialog.close();
                            }
                        });
                        
                        Button cancelDialogButton = new Button("Cancel");
                        cancelDialogButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent event) {
                                dialog.close();
                            }
                        });
                        buttons.getChildren().addAll(okDialogButton, cancelDialogButton);

                        box.getChildren().addAll(new Label("What you wanna find?"), hBox, buttons);
                        Scene scene1 = new Scene(box);
                        dialog.setScene(scene1);
                        dialog.show();

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            hBox.getChildren().add(findButton);

            Button editButton = new Button("", editImageView);
            editButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent event) {
                            try{
                                final int index;
                                ObservableList<TablePosition> data2;
                                data2 = tableView.getSelectionModel().getSelectedCells();
                                index = data2.get(0).getRow();

                                final Stage dialog = new Stage();

                                VBox box = new VBox();
                                box.setAlignment(Pos.CENTER);

                                //Стоблец с полями для ввода
                                VBox vBox = new VBox(); 
                                final TextField id = new TextField(String.valueOf(client.data.get(index).getId()));
                                id.setPromptText("Enter id");
                                final TextField name = new TextField(client.data.get(index).getName());
                                name.setPromptText("Enter name");
                                final TextField publisher = new TextField(client.data.get(index).getPublisher());
                                publisher.setPromptText("Enter publisher");
                                final TextField date = new TextField(String.valueOf(client.data.get(index).getDate()));
                                date.setPromptText("Enter date");
                                final TextField pages = new TextField(String.valueOf(client.data.get(index).getPages()));
                                pages.setPromptText("Enter number of pages");
                                final TextField smooth = new TextField(String.valueOf(client.data.get(index).getSmooth()));
                                smooth.setPromptText("Is it smooth?");
                                vBox.getChildren().addAll(id, name, publisher, date, pages, smooth);

                                HBox buttons = new HBox();
                                buttons.setAlignment(Pos.CENTER);
                                Button addDialogButton = new Button("Add");
                                addDialogButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent event) {
                                        try {
                                            Book b = new Book(Integer.parseInt(id.getText()),name.getText(),publisher.getText(), Integer.parseInt(date.getText()), Integer.parseInt(pages.getText()), Boolean.parseBoolean(smooth.getText()));
                                            id.clear();
                                            name.clear();
                                            publisher.clear();
                                            date.clear();
                                            pages.clear();
                                            smooth.clear();
                                            
                                            
                                            client.edit(index, b);

                                        }catch (NumberFormatException e){
                                            System.err.println("NaN! Try again!");
                                        }
                                        dialog.close();
                                    }
                                });

                                Button cancelDialogButton = new Button("Cancel");
                                cancelDialogButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent event) {
                                        dialog.close();
                                    }
                                });
                                buttons.getChildren().addAll(addDialogButton, cancelDialogButton);

                                box.getChildren().addAll(new Label("Add a note"), vBox, buttons);
                                Scene scene1 = new Scene(box);
                                dialog.setScene(scene1);
                                dialog.show();
                            }catch(Exception e){
                                final Stage error = new Stage();
                                    VBox box = new VBox();
                                    box.setAlignment(Pos.CENTER);
                                    final Label date = new Label("At first, select line which you want to modify");
                                    Button okDialogButton = new Button("Ok");
                                    okDialogButton.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent event) {
                                            error.close();
                                        }
                                     });
                                    box.getChildren().addAll(date, okDialogButton);
                                    Scene scene1 = new Scene(box);
                                    error.setScene(scene1);
                                    error.show();
                            }
                            
                                    
                            
                        }
                      });
            hBox.getChildren().add(editButton);

            //Кнопка обновления
            Button refreshButton = new Button("", refreshImageView);
            refreshButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent event) {
                    try{
                        client.Update();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            hBox.getChildren().add(refreshButton);

            VBox vBox = new VBox();
            vBox.getChildren().addAll(hBox, tableView);

            ((Group) scene.getRoot()).getChildren().addAll(vBox);

            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){
        client.disconnetc(client);
    }

    public static void main(String[] args) throws Exception{
        launch(args);
    }
}