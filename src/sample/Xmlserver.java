package sample;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

//UnicastRemoteObject
public class Xmlserver extends UnicastRemoteObject implements IServer {
    
    public Xmlserver() throws RemoteException{
        super();
    }
    
 
    public static List<Book> Domread() throws Exception{
        DOMParser parser = new DOMParser();
        parser.parse("src/sample/baze.xml");
        Document document = parser.getDocument();
        
        List<Book> booklist = new ArrayList<>();
        
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Book book = new Book(0, "","",0,0,true);
                book.setId(Integer.parseInt(node.getAttributes().getNamedItem("id").getNodeValue()));
 
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node cNode = childNodes.item(j);
 
                    if (cNode instanceof Element) {
                        String content = cNode.getLastChild().getTextContent().trim();
                        switch (cNode.getNodeName()) {
                            case "name":
                                book.setName(content);
                                break;
                            case "publisher":
                                book.setPublisher(content);
                                break;
                            case "date":
                                book.setDate(Integer.parseInt(content));
                            break;
                            case "pages":
                                book.setPages(Integer.parseInt(content));
                            break;
                            case "smooth":
                                book.setSmooth(Boolean.parseBoolean(content));
                            break;
                        }
                    }
                }
                booklist.add(book);
            }
        }
        return booklist;
    }
    
    public static void Domwrite(Book book) throws Exception{
        DOMParser parser = new DOMParser();
        parser.parse("src/sample/baze.xml");
        Document doc = parser.getDocument();
        
        Node root = doc.getFirstChild();

        Element data = doc.createElement("Book");
        data.setAttribute("id", Integer.toString(book.getId()));
        root.appendChild(data);

        Element name = doc.createElement("name");
        name.setTextContent(book.getName());
        data.appendChild(name);

        Element pub = doc.createElement("publisher");
        pub.setTextContent(book.getPublisher());
        data.appendChild(pub);

        Element date = doc.createElement("date");
        date.setTextContent(Integer.toString(book.getDate()));
        data.appendChild(date);

        Element str = doc.createElement("pages");
        str.setTextContent(Integer.toString(book.getPages()));
        data.appendChild(str);

        Element smooth = doc.createElement("smooth");
        smooth.setTextContent(Boolean.toString(book.getSmooth()));
        data.appendChild(smooth);

        doc = parser.getDocument();
        OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        XMLSerializer serializer = new XMLSerializer(new FileOutputStream(new File("src/sample/baze.xml")), format);
        serializer.serialize(doc);
        

    }
    
    public static boolean Domdelete(int id) throws Exception{
        DOMParser parser = new DOMParser();
        parser.parse("src/sample/baze.xml");
        Document doc = parser.getDocument();

        Node root = doc.getFirstChild();
//        System.out.println(root.getNodeName());
        NodeList child = root.getChildNodes();
        System.out.println(child.getLength());
//        for (int i = 0; i < child.getLength()/2; i++) {
//        System.out.println(child.item(i).getNodeName());
//        }
//        System.out.println(child.item(1).getAttributes().getNamedItem("id").getNodeValue());
        for (int i = 1; i < child.getLength(); i=i+2) {
            if (Integer.parseInt(child.item(i).getAttributes().getNamedItem("id").getNodeValue()) == id){
                System.out.printf("удалил");
//                child.item(i).removeChild(root);
                root.removeChild(child.item(i));
                        doc = parser.getDocument();
        OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        XMLSerializer serializer = new XMLSerializer(new FileOutputStream(new File("src/sample/baze.xml")), format);
        serializer.serialize(doc);
                return true;
            }
                
        }
        
//        System.out.printf(child.item(0).getAttributes().getNamedItem("id").getNodeValue());
        return false;
    }
//    public static ServerSocket listener;
//    public static ObjectInputStream deserializer;
//    public static ObjectOutputStream serializer;
    
    
    public static List<Book> arrayList = new ArrayList<Book>();
    
    public String SayHello(String text) throws RemoteException{
        text = "Hi " + text;
        return text;
    }
    
    public void AddData(Book ex){
        System.out.println("Object get");
        try{
            Domwrite(ex);    
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("Object sucsseccfuly writed");
    } 
   
    
    public List<Book> print(){
        try{
            System.out.println("Object sucsseccfuly sent");
            return Domread();    
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    public boolean DeleteData(int kol){
        boolean bool=false;
        try{
            Domread();   
            bool = Domdelete(kol);
        }catch (Exception e){
            System.out.println(e);
        }
        if (bool){
            System.out.println("Object sucsseccfuly deleted");
            return true;
        }
        else{
        System.out.println("Can't delete. Object doesn't exist!");
        return false;
        }
    }
    
    public  ArrayList<Book> search(String ser, int mode){
        ArrayList<Book> found = new ArrayList<>();
        int i=0;
        System.out.println("Search request get");
        
        try{
            arrayList = Domread();    
        }catch (Exception e){
            System.out.println(e);
        }
        
        switch (mode){
            case 1:
                while (i<arrayList.size()){
                if (arrayList.get(i).getName().equals(ser))
                    found.add(arrayList.get(i));
                i++;
                }
            break;
            case 2:
                while (i<arrayList.size()){
                if (arrayList.get(i).getPublisher().equals(ser))
                    found.add(arrayList.get(i));
                i++;
                }
            break;
            case 3:
                while (i<arrayList.size()){
                if (arrayList.get(i).getDate()==Integer.parseInt(ser))
                    found.add(arrayList.get(i));
                i++;
                }
            break;
            case 4:
                while (i<arrayList.size()){
                if (arrayList.get(i).getPages()==Integer.parseInt(ser))
                    found.add(arrayList.get(i));
                i++;
                }
            break;
        }
        return found;
    }

    public void edit(int id, Book book){
        try{
        DOMParser parser = new DOMParser();
        parser.parse("src/sample/baze.xml");
        Document doc = parser.getDocument();

        Node root = doc.getFirstChild();
//        System.out.println(root.getNodeName());
        NodeList child = root.getChildNodes();
        System.out.println(id);
//        for (int i = 0; i < child.getLength(); i++) {
//        System.out.println(child.item(i).getNodeName());
//        }
//        System.out.println(child.item(1).getAttributes().getNamedItem("id").getNodeValue());
        
//        Node node = doc.getFirstChild();
//        node.

        child.item(id*2+1).getAttributes().getNamedItem("id").setNodeValue(String.valueOf(book.getId()));
        NodeList node = child.item(id*2+1).getChildNodes();
        node.item(1).setTextContent(book.getName());
        node.item(3).setTextContent(book.getPublisher());
        node.item(5).setTextContent(String.valueOf(book.getDate()));
        node.item(7).setTextContent(String.valueOf(book.getPages()));
        node.item(9).setTextContent(String.valueOf(book.getSmooth()));

       
                        doc = parser.getDocument();
        OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        XMLSerializer serializer = new XMLSerializer(new FileOutputStream(new File("src/sample/baze.xml")), format);
        serializer.serialize(doc);
                
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*@Override
    public void registry(IClient client) {
        client.Update();
    }*/

    public static void main(String[] args){
        try{
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("server", new Xmlserver());
            System.out.println("Server started");
            
//            listener = new ServerSocket(8000);
//            listener.setSoTimeout(800000);
//            Socket client = listener.accept();
//            deserializer = new ObjectInputStream(client.getInputStream());
//            serializer = new ObjectOutputStream(client.getOutputStream());
        }catch (Exception e){
            System.out.println(e);
        }
    }
}