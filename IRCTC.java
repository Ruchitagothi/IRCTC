import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

class IRCTC{
    static Scanner sc=new Scanner(System.in);
    static String url = "jdbc:mysql://Localhost:3306/ruchita";
    static String uname = "root";
    static String password = "ruchita123";
    static  Connection con;

    public static void main(String[] args) throws Exception{
        con= DriverManager.getConnection(url, uname, password);
        Statement st=con.createStatement();
        while(true){
            System.out.println("Welcome to IRCTC");
            System.out.println("What would you like to do?");
            System.out.println("1. Login");
            System.out.println("2. Sign_up");
            System.out.println("3. Exit");
            int x;
            try {
                x=sc.nextInt();
            }
            catch (InputMismatchException e){
                x=sc.nextInt();
            }
            sc.nextLine();
            switch(x){
                case 1:
                    System.out.println("Enter email");
                    String email=sc.nextLine();
                    while(!checkEmail(email)){
                        System.out.println("Invalid email_id please enter again");
                        email = sc.nextLine();
                    }
                    System.out.println("Enter pass");
                    String pass=sc.nextLine();
                    while(!checkPass(email,pass)){
                        System.out.println("Invalid password  please enter again");
                        pass=sc.nextLine();
                    }
                    ResultSet r=st.executeQuery("Select email,name from login_irctc;");
                    String name="";
                    while (r.next()){
                        if(r.getString(1).equals(email)){
                            name=r.getString(2);
                        }
                    }
                    User u=new User(email,name,con);
                    break;
                case 2:
                    System.out.println("Enter Email");
                    email=sc.nextLine();
                    while(!satisfyEmail(email)){
                        System.out.println("Please enter a valid email");
                        email=sc.nextLine();
                    }
                    if(checkEmail(email)){
                        String sql="select name from login_irctc where email='"+email+"'";
                        r=st.executeQuery(sql);
                        name="";
                        while (r.next()){
                            name=r.getString(1);
                        }
                        System.out.println("Email is already registered plz enter password");
                        pass=sc.nextLine();
                        while(!checkPass(email,pass)){
                            System.out.println("Invalid password enter again");
                            pass=sc.nextLine();
                        }
                    }
                    else{
                        System.out.println("Enter password,(password must contain letters and digits and should have more than 8 characters)");
                        pass=sc.nextLine();
                        while(!satisfyPass(pass)){
                            System.out.println("Invalid format of password,enter again");
                            pass=sc.nextLine();
                        }
                        System.out.println("Enter your name");
                        name=sc.nextLine();
                        String s="Insert into login_irctc values('"+email+"','"+pass+"','"+name+"');";
                        st.execute(s);
                    }
                    u=new User(email,name,con);
                    break;
                case 3:
                    System.exit(0);

            }

        }

    }
    static boolean satisfyEmail(String email){
        if(email.endsWith("@gmail.com")&&(!email.startsWith("@gmail.com"))){
            return true;
        }
        else {
            return  false;
        }
    }
    static boolean satisfyPass(String pass){
        boolean letter=false;
        boolean digit=false;
        if(pass.length()>=8){
            for(char c:pass.toCharArray()){
                if(Character.isAlphabetic(c)){
                    letter=true;
                }
                if(Character.isDigit(c)){
                    digit=true;
                }
            }
        }
        if(letter&&digit){
            return true;
        }
        else{
            return false;
        }
    }
    static boolean checkEmail(String email) throws Exception{
        Statement st= con.createStatement();
        ArrayList<String> a1 =new ArrayList<>();
        String s="SELECT email from login_irctc;";
        ResultSet r=st.executeQuery(s);
        while(r.next()){
            a1.add(r.getString(1));
        }
        if(a1.contains(email)) {
            return true;
        }
        else{
            return false;
        }
    }
    static boolean checkPass(String email,String pass)throws Exception{
        Statement st= con.createStatement();
        HashMap<String,String> hm=new HashMap<>();
        String s="SELECT email,pass,name from login_irctc;";
        ResultSet r=st.executeQuery(s);
        String name="";
        while(r.next()){
            hm.put(r.getString(1),r.getString(2));
            if(email.equals(r.getString(1))){
                name=r.getString(3);
            }
        }
        if(hm.get(email).equals(pass)){
            System.out.println("Welcome "+name);
            return true;
        }
        else{
            return false;
        }
    }
}
class User{
    private static Connection con;

    static Scanner sc=new Scanner(System.in);
    static private String email;
    static private String name;

    public User(String email, String name,Connection con) throws Exception {
        this.con=con;
        this.email = email;
        this.name = name;
        start();
    }
    private static void start() throws Exception{
        boolean check=true;
        while(check){
            System.out.println("\n\nEnter 1 for booking ticket");
            System.out.println("Enter 2 for cancelling a ticket");
            System.out.println("Enter 3 for viewing all trains");
            System.out.println("Enter 4 for searching train by source");
            System.out.println("Enter 5 for searching train by destination");
            System.out.println("Enter 6 for Logout");
            int input=sc.nextInt();
            while(input>6||input<1){
                System.out.println("Enter a valid input");
                input= sc.nextInt();
            }
            sc.nextLine();
            switch(input){
                case 1:
                    book();
                    break;
                case 2:
                    cancelTicket();
                    break;
                case 3:
                    display_All_Trains();
                    break;
                case 4:
                    searchBySource();
                    break;
                case 5:
                    searchByDestination();
                    break;
                case 6:
                    check=false;
                    break;
            }
        }
    }
    static void book() throws Exception{
        Statement st= con.createStatement();
        System.out.println("Enter source");
        String source=sc.nextLine();
        System.out.println("Enter Destination");
        String dest=sc.nextLine();
        String sql="select count(*) from IRCTC where source='"+ source +"' and destination='"+ dest +"';";
        ResultSet r=st.executeQuery(sql);
        boolean is_available=false;
        while (r.next()){
            if(r.getInt(1)>0){
                is_available=true;
            }
        }
        if(!is_available){
            System.out.println("Sorry no trains for your choice please try again");
            System.out.println("Enter 1 for trying again");
            System.out.println("Enter 2 for exit");
            int input=sc.nextInt();
            sc.nextLine();
            while (input>2||input<1){
                System.out.println("Enter a valid input");
                input=sc.nextInt();
                sc.nextLine();
            }
            switch (input){
                case 1:
                    book();
                    break;
                case 2:
                    start();
                    break;
            }
        }
        else{
            sql="select name from IRCTC where source='"+ source +"' and destination='"+ dest +"';";
            r=st.executeQuery(sql);
            String t_name="";
            while (r.next()){
                t_name=r.getString(1);
            }
            sql="Select journey_date from "+ t_name +" where seats_remaining>0;";
            r=st.executeQuery(sql);
            System.out.println("Available dates for your journey");
            while (r.next()){
                System.out.println(r.getString(1));
            }
            System.out.println("Enter date of your choice in same format");
            String date= sc.nextLine();
            while(!checkDate(date)){
                System.out.println("Invalid format of date enter again");
                date= sc.nextLine();
            }
            sql="select count(*) from "+t_name+" where journey_date='"+date+ "'";
            r=st.executeQuery(sql);
            boolean is_valid=false;
            while (r.next()){
                if(r.getInt(1)>0){
                    is_valid=true;
                }
            }
            while (!is_valid){
                System.out.println("Please enter valid date");
                date= sc.nextLine();
                sql="select count(*) from "+t_name+" where date='"+date+ "';";
                r=st.executeQuery(sql);
                while (r.next()){
                    if(r.getInt(1)>0){
                        is_valid=true;
                    }
                }
            }
            sql="select seats_remaining from "+t_name+" where journey_date='"+ date+"';";
            r=st.executeQuery(sql);
            int seats_remaining=0;
            while (r.next()){
                seats_remaining=r.getInt(1);
            }
            System.out.println("we have "+ seats_remaining+" seats remaining");
            System.out.println("How many would you like to buy?");
            int seats_purchased=sc.nextInt();
            sc.nextLine();
            while(seats_purchased>seats_remaining || seats_purchased<0){
                System.out.println("Insufficient seats please enter again");
                seats_purchased=sc.nextInt();
                sc.nextLine();
            }
            sql="update "+ t_name+" set seats_remaining=seats_remaining-"+seats_purchased+" where journey_date='"+date+"'";
            st.execute(sql);
            sql="select max(b_id) from bookings";
            r=st.executeQuery(sql);
            int b_id=0;
            while(r.next()){
                b_id=r.getInt(1)+1;
            }
            sql="insert into bookings values("+b_id+",'"+name+"','"+email+"',"+seats_purchased +",'"+date+"','"+source+"','"+dest+"','"+t_name+"')";
            st.execute(sql);
            String p_name="";
            for(int i=0;i<seats_purchased;i++){
                System.out.println("Enter passenger "+(i+1)+" name");
                p_name=sc.nextLine();
                System.out.println("Enter gender");
                String gender=sc.nextLine();
                System.out.println("Enter age");
                int age=sc.nextInt();
                while (age<0){
                    System.out.println("Enter valid age");
                    age=sc.nextInt();
                }
                sc.nextLine();
                sql="insert into passengers(name,gender,age,b_id) values(?,?,?,?)";
                PreparedStatement pst=con.prepareCall(sql);
                pst.setString(1,p_name);
                pst.setString(2,gender);
                pst.setInt(3,age);
                pst.setInt(4,b_id);
                pst.execute();

            }
            System.out.println(seats_purchased+" tickets will cost "+(seats_purchased*2000)+" rs");
            System.out.println("Enter credit card number");
            String card_no=sc.nextLine();
            while (!card_no.equals("1234")){
                System.out.println("Invalid card number enter again");
                System.out.println(card_no);
                card_no=sc.nextLine();
            }
            System.out.println("Enter password");
            int password=sc.nextInt();
            sc.nextLine();
            while (password!=1234){
                System.out.println("Invalid password enter again");
                password=sc.nextInt();
                sc.nextLine();
            }
            System.out.println("Payment successful");
            sql="Select pnr_number from passengers where name='"+p_name+"'";
            ResultSet rs=st.executeQuery(sql);
            while (rs.next()){
                System.out.println("Pnr number for "+p_name+" is "+ rs.getInt(1));
            }

        }
    }
    static void cancelTicket()throws Exception{
        String sql="Select * from bookings where email=?";
        PreparedStatement pst=con.prepareCall(sql);
        pst.setString(1,email);
        ResultSet rs=pst.executeQuery();
        System.out.println("Your bookings");
        ArrayList<Integer>booking_id=new ArrayList<Integer>();
        while (rs.next()){
            booking_id.add(rs.getInt(1));
            System.out.println("Booking id: "+rs.getInt(1)+" Date: "+rs.getString(5)+" seats booked: "+rs.getInt(4)+" from "
                    +rs.getString("from") +" to "+rs.getString("to") );

        }
        System.out.println("Enter booking id which you want to delete");
        int bid=sc.nextInt();
        while (!booking_id.contains(bid)){
            System.out.println("Enter a valid booking id");
            bid= sc.nextInt();
        }
        sc.nextLine();
        sql="select train,seats,journey_date from bookings where b_id=?";
        pst= con.prepareCall(sql);
        pst.setInt(1,bid);
        rs=pst.executeQuery();
        String t_name="";
        int seats=0;
        String date="";
        while (rs.next()){
            t_name=rs.getString(1);
            seats=rs.getInt(2);
            date=rs.getString(3);
        }
        sql="delete from passengers where b_id="+bid;
        pst.execute(sql);
        sql="delete from bookings where b_id= "+bid;
        pst.execute(sql);
        sql="update "+ t_name+ " set seats_remaining=seats_remaining+? where journey_date=?";
        pst= con.prepareCall(sql);
        pst.setInt(1,seats);
        pst.setString(2,date);
        pst.execute();
        System.out.println("Ticket cancelled successfully");
        System.out.println("Your money will be refunded in your account");
    }
    static void display_All_Trains() throws Exception{
        String sql="select * from IRCTC ";
        Statement st= con.createStatement();
        ResultSet rs=st.executeQuery(sql);
        while(rs.next()){
            System.out.println("Train No: "+rs.getString(1)+" Train name: "+rs.getString(2)+ " from "+rs.getString("source")+" to "+rs.getString("destination"));
        }
    }
    static void searchByDestination()throws Exception {
        System.out.println("Where do you want to go?");
        String dest= sc.nextLine();
        String sql="select count(*) from IRCTC where destination='"+dest+"'";
        Statement st= con.createStatement();
        ResultSet rs=st.executeQuery(sql);
        boolean flag=false;
        while (rs.next()){
            if(rs.getInt(1)==0){
                System.out.println("Sorry no trains for your destination");
            }
            else{
                flag=true;
                System.out.println("We have "+rs.getInt(1)+" trains to "+ dest);
            }
        }
        if(flag){
            sql="select * from IRCTC where destination='"+dest+"'";
            rs=st.executeQuery(sql);
            while (rs.next()){
                System.out.println("Train name: "+rs.getString("name")+" from "+rs.getString("source")+" to "+dest);
            }
        }
    }
    static void searchBySource()throws Exception {
        System.out.println("Enter source?");
        String source= sc.nextLine();
        String sql="select count(*) from IRCTC where source='"+source+"'";
        Statement st= con.createStatement();
        ResultSet rs=st.executeQuery(sql);
        boolean flag=false;
        while (rs.next()){
            if(rs.getInt(1)==0){
                System.out.println("Sorry no trains for your source");
            }
            else{
                flag=true;
                System.out.println("We have "+rs.getInt(1)+" train from "+ source);
            }
        }
        if(flag){
            sql="select * from IRCTC where source='"+source+"'";
            rs=st.executeQuery(sql);
            while (rs.next()){
                System.out.println("Train name: "+rs.getString("name")+" from "+source+" to "+rs.getString("destination"));
            }
        }
    }
    static boolean checkDate(String date){
        if(date.length()==10){
            if(date.charAt(4)=='-'&& date.charAt(7)=='-'){
                return true;
            }
        }
        return false;
    }
}