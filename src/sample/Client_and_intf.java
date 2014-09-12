package sample;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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

public class Client_and_intf extends Application{
    
    //Подгружаем иконки для кнопок
    private static final Image ICON_ADD = new Image(Client_and_intf.class.getResourceAsStream("add.png"));
    private static final Image ICON_DELETE = new Image(Client_and_intf.class.getResourceAsStream("delete.png"));
    private static final Image ICON_FIND = new Image(Client_and_intf.class.getResourceAsStream("find.png"));
    private static final Image ICON_EDIT = new Image(Client_and_intf.class.getResourceAsStream("edit.png"));
    private static final Image ICON_REFRESH = new Image(Client_and_intf.class.getResourceAsStream("refresh.png"));

    final ObservableList<Book> data = FXCollections.observableArrayList();
    Registry reg;
    IServer rmi;

    //То, что будет выполнено при старте приложения
    @Override public void start(Stage primaryStage) throws Exception {
        try{
            //Делаем из них объект ImageView
            ImageView addImageView = new ImageView(ICON_ADD);
            ImageView deleteImageView = new ImageView(ICON_DELETE);
            ImageView findImageView = new ImageView(ICON_FIND);
            ImageView editImageView = new ImageView(ICON_EDIT);
            ImageView refreshImageView = new ImageView(ICON_REFRESH);

            //Ищем сервер и подсоеденяем его
            reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
            rmi = (IServer) reg.lookup("server");
            System.out.println("Connected");
            String text = rmi.SayHello("Master");
            //rmi.registry(new Client_and_intf());
            System.out.println(text);

            //При старте сразу считываем то, что уже храниться в наей базе
            data.addListener(new ListChangeListener<Book>() {
                @Override
                public void onChanged(Change<? extends Book> c) {
                    //TODO
                }
            });
            List<Book> booklist = new ArrayList<>();
            booklist=rmi.print();
            for(int i=0; i<booklist.size();i++){
                data.add(booklist.get(i));
            }
            
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
//            nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//            nameCol.setOnEditCommit(
//                new EventHandler<CellEditEvent<Book, String>>() {
//                @Override
//                public void handle(CellEditEvent<Book, String> t) {
//                    ((Book) t.getTableView().getItems().get(
//                    t.getTablePosition().getRow())
//                    ).setName(t.getNewValue());
//                    
//            }
//        }
//    );

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
//            tableView.setMaxHeight(300);
            tableView.setMaxWidth(395);
            tableView.setItems(data);
            tableView.getColumns().addAll(idCol, nameCol, publisherCol, dateCol, pagesCol, smoothCol);
            
//            ObservableList<Book> data2 = FXCollections.observableArrayList();
//            data2 = tableView.getSelectionModel().getSelectedCells();
//            for(int i=0; i<data2.size();i++){
//                System.out.print(data2.get(i));
//            }
            
            
            
            
            
            

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
                                    rmi.AddData(b);
                                    List<Book> booklist;
                                    booklist=rmi.print();
                                    data.clear();
                                    for(int i=0; i<booklist.size();i++){
                                        data.add(booklist.get(i));
                                    }
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
                                }catch (RemoteException ex) {
                                    Logger.getLogger(Client_and_intf.class.getName()).log(Level.SEVERE, null, ex);
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
                            
                            
                            List<Book> booklist;
                            booklist=rmi.print();
                            rmi.DeleteData(booklist.get(index).getId());
                            
                            booklist=rmi.print();
                            data.clear();
                            for(int i=0; i<booklist.size();i++){
                                data.add(booklist.get(i));
                            }

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
                                    List<Book> booklist = new ArrayList<>();
                                    if (cb.getValue()=="Name")
                                        booklist = rmi.search(number.getText(), 1);
                                    if (cb.getValue()=="Publisher")
                                        booklist = rmi.search(number.getText(), 2);
                                    if (cb.getValue()=="Date")
                                        booklist = rmi.search(number.getText(), 3);
                                    if (cb.getValue()=="Pages")
                                        booklist = rmi.search(number.getText(), 4);
                                    number.clear();
                                    data.clear();
                                    for(int i=0; i<booklist.size();i++){
                                        data.add(booklist.get(i));
                                    }
                                }catch (NumberFormatException e){
                                    System.err.println("NaN! Try again!");
                                } catch (RemoteException ex) {
                                    Logger.getLogger(Client_and_intf.class.getName()).log(Level.SEVERE, null, ex);
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
                            ObservableList<TablePosition> data2 = FXCollections.observableArrayList();
                            data2 = tableView.getSelectionModel().getSelectedCells();
                            index = data2.get(0).getRow();
                            
                            
                            List<Book> booklist;
                            booklist=rmi.print();
                            
                            
                            
                                final Stage dialog = new Stage();

                                VBox box = new VBox();
                                box.setAlignment(Pos.CENTER);

                                //Стоблец с полями для ввода
                                VBox vBox = new VBox(); 
                                final TextField id = new TextField(String.valueOf(booklist.get(index).getId()));
                                id.setPromptText("Enter id");
                                final TextField name = new TextField(booklist.get(index).getName());
                                name.setPromptText("Enter name");
                                final TextField publisher = new TextField(booklist.get(index).getPublisher());
                                publisher.setPromptText("Enter publisher");
                                final TextField date = new TextField(String.valueOf(booklist.get(index).getDate()));
                                date.setPromptText("Enter date");
                                final TextField pages = new TextField(String.valueOf(booklist.get(index).getPages()));
                                pages.setPromptText("Enter number of pages");
                                final TextField smooth = new TextField(String.valueOf(booklist.get(index).getSmooth()));
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
                                            
                                            
                                            rmi.edit(index, b);
                                            
                                            
                                            
                                            List<Book> booklist;
                                            booklist=rmi.print();
                                            data.clear();
                                            for(int i=0; i<booklist.size();i++){
                                                data.add(booklist.get(i));
                                            }
                                        }catch (NumberFormatException e){
                                            System.err.println("NaN! Try again!");
                                        }catch (RemoteException ex) {
                                            Logger.getLogger(Client_and_intf.class.getName()).log(Level.SEVERE, null, ex);
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
                        List<Book> booklist = new ArrayList<>();
                        booklist=rmi.print();
                        data.clear();
                        for(int i=0; i<booklist.size();i++){
                            data.add(booklist.get(i));
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            hBox.getChildren().add(refreshButton);

            VBox vBox = new VBox();
//            vBox.setSpacing(5);
//            vBox.setPadding(new Insets(10, 0, 0, 10));
            vBox.getChildren().addAll(hBox, tableView);

            ((Group) scene.getRoot()).getChildren().addAll(vBox);

            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        launch(args);
        Client client = new Client();
    }

    /*@Override
    public void Update() {
        try{
            List<Book> booklist = new ArrayList<>();
            booklist=rmi.print();
            data.clear();
            for(int i=0; i<booklist.size();i++){
                data.add(booklist.get(i));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
}