package databasemanagement;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseAccessor {
    private Connection con;
    private String url, database, user, password;


    public DatabaseAccessor(String url, String database, String user, String password){
        this.url = url;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public void connect(){
        // Set up connection url
        String connectionUrl = "jdbc:mysql://" + url + "/" + database;

        // Getting connection
        try{
            con = DriverManager.getConnection(connectionUrl, user, password);
            System.out.println("Connected");
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getCon(){
        return con;
    }

    private ResultSet select(String table, String col, String val){
        try{
            // Prepare query statement
            PreparedStatement ps = con.prepareStatement("SELECT * FROM "+ table +" WHERE " + col + " = " + val);
            return ps.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public String[] searchRecord(String table, int id){
        // check if column size is not -1
        if(numCols(table) < 0)
            return null;
        else {
            String[] r = new String[numCols(table)];
            try{
                // get result set
                ResultSet rs = select(table, "id", Integer.toString(id));

                // placing into return String[]
                rs.next();
                for(int i = 1; i <= r.length; i++){
                    r[i-1] = rs.getString(i);
                }
                return r;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    public String[] getRecord(String table, int index){
        // prepare query string
        String query = "SELECT * FROM " + table + " LIMIT " + index + ",1";

        try{
            if(numCols(table) < 0)
                return null;
            else{
                String[] r = new String[numCols(table)];
                // prepare query statement
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                // inserting into result array
                for(int i = 1; i <= r.length; i++)
                    r[i - 1] = rs.getString(i);
                return r;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public int numCols(String table){
        // Prepare query string
        String query = "SELECT count(*)\n" +
                "FROM information_schema.columns\n" +
                "WHERE table_name = '" + table + "'";

        try{
            // Prepare query statement
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<String[]> selectTable(String table){
        ArrayList<String[]> r = new ArrayList<>();

        // prepare query string
        String query = "SELECT * FROM " + table;

        try {
            // prepare query statement
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            // storing result set
            int numCols = numCols(table);
            while(rs.next()){
                String[] row = new String[numCols];
                for(int i = 0; i < row.length; i++){
                    row[i] = rs.getString(i + 1);
                }
                r.add(row);
            }

            return r;
        }catch(SQLException e){
            e.printStackTrace();

            return null;
        }
    }
}
