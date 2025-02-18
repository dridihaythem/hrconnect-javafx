package services;

import models.Formateur;
import models.Formation;
import utils.MyDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FormationService implements  Crud<Formation> {

    Connection conn;

    public FormationService(){
        this.conn = MyDb.getInstance().getConn();
    }

    @Override
    public void create(Formation obj) throws Exception {
        String sql = "insert into formations (formateur_id,title,description,image,is_online,place,available_for_employee,available_for_intern,start_date,end_date) VALUES (?,?, ?, ?,?, ?, ?, ?, ?, ?)";
        PreparedStatement  stmt = conn.prepareStatement(sql);
        stmt.setInt(1,obj.getFormateur_id());
        stmt.setString(2, obj.getTitle());
        stmt.setString(3, obj.getDescription());
        stmt.setString(4, obj.getImage());
        stmt.setBoolean(5, obj.isIs_online());
        stmt.setString(6, obj.getPlace());
        stmt.setBoolean(7, obj.isAvailable_for_employee());
        stmt.setBoolean(8, obj.isAvailable_for_intern());
        stmt.setTimestamp(9, new java.sql.Timestamp(obj.getStart_date().getTime()));
        if (obj.getEnd_date() != null) {
            stmt.setTimestamp(10, new java.sql.Timestamp(obj.getEnd_date().getTime()));
        } else {
            stmt.setNull(10, java.sql.Types.TIMESTAMP);
        }
        stmt.executeUpdate();
    }

    @Override
    public void update(Formation obj) throws Exception {
        System.out.println(obj);
        String sql = "update formations set title = ?, description = ?, image = ?, is_online = ?, place = ? , available_for_employee = ?, available_for_intern = ?, start_date = ?, end_date = ? where id = ?";
        PreparedStatement  stmt = conn.prepareStatement(sql);
        stmt.setString(1, obj.getTitle());
        stmt.setString(2, obj.getDescription());
        stmt.setString(3, obj.getImage());
        stmt.setBoolean(4, obj.isIs_online());
        if(obj.getPlace() != null){
            stmt.setString(5, obj.getPlace());
        }else{
            stmt.setNull(5, java.sql.Types.VARCHAR);
        }
        stmt.setBoolean(6, obj.isAvailable_for_employee());
        stmt.setBoolean(7, obj.isAvailable_for_intern());
        stmt.setTimestamp(8, new java.sql.Timestamp(obj.getStart_date().getTime()));
        if (obj.getEnd_date() != null) {
            stmt.setTimestamp(9, new java.sql.Timestamp(obj.getEnd_date().getTime()));
        } else {
            stmt.setNull(9, java.sql.Types.TIMESTAMP);
        }
        stmt.setInt(10, obj.getId());
        stmt.executeUpdate();
    }

    @Override
    public void delete(Formation obj) throws Exception {
        String sql = "delete from formations where id = ?";
        PreparedStatement  stmt = conn.prepareStatement(sql);
        stmt.setInt(1, obj.getId());
        stmt.executeUpdate();
    }

    public void delete(int id) throws Exception {
        String sql = "delete from formations where id = ?";
        PreparedStatement  stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    @Override
    public List<Formation> getAll() throws Exception {
        String sql = "select * from formations ORDER BY id DESC";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            Formation formation = new Formation();
            formation.setId(rs.getInt("id"));
            formation.setTitle(rs.getString("title"));
            formation.setDescription(rs.getString("description"));
            formation.setImage(rs.getString("image"));
            formation.setIs_online(rs.getBoolean("is_online"));
            formation.setPlace(rs.getString("place"));
            formation.setAvailable_for_employee(rs.getBoolean("available_for_employee"));
            formation.setAvailable_for_intern(rs.getBoolean("available_for_intern"));
            formation.setStart_date(rs.getTimestamp("start_date"));
            if(rs.getTimestamp("end_date") != null){
                formation.setEnd_date(rs.getTimestamp("end_date"));
            }else{
                formation.setEnd_date(null);
            }
            formations.add(formation);
        }
        return formations;
    }

    public List<Formation> getAllFormationsForUser() throws Exception {

        String sql = "SELECT a.id, a.title, a.image, a.is_online, a.description, a.place, a.start_date, a.end_date, b.first_name, b.last_name FROM formations a JOIN formateurs b ON a.formateur_id = b.id WHERE a.id NOT IN ( SELECT formation_id FROM formation_participation WHERE employe_id = 18 ) ORDER BY a.id DESC";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            Formation formation = new Formation();
            formation.setId(rs.getInt("id"));
            formation.setTitle(rs.getString("title"));
            formation.setDescription(rs.getString("description"));
            formation.setImage(rs.getString("image"));
            formation.setIs_online(rs.getBoolean("is_online"));
            formation.setPlace(rs.getString("place"));
            formation.setStart_date(rs.getTimestamp("start_date"));
            if(rs.getTimestamp("end_date") != null){
                formation.setEnd_date(rs.getTimestamp("end_date"));
            }else{
                formation.setEnd_date(null);
            }
            Formateur formateur = new Formateur();
            formateur.setFirstName(rs.getString("first_name"));
            formateur.setLastName(rs.getString("last_name"));
            formation.setFormateur(formateur);
            formations.add(formation);
        }
        return formations;
    }

    public void participerFormation(int formation_id,int employe_id) throws Exception {
        String sql = "insert into  formation_participation (formation_id,employe_id) VALUES (?,?)";
        PreparedStatement  stmt = conn.prepareStatement(sql);
        stmt.setInt(1, formation_id);
        stmt.setInt(2, employe_id);
        stmt.executeUpdate();
    }

    public List<Formation> getMesFormation(int employe_id) throws Exception {

        String sql = "SELECT a.id, a.title, a.image, a.is_online, a.description, a.place, a.start_date, a.end_date, b.first_name, b.last_name FROM formations a JOIN formateurs b ON a.formateur_id = b.id WHERE a.id IN ( SELECT formation_id FROM formation_participation WHERE employe_id = 18 ) ORDER BY a.start_date DESC";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            Formation formation = new Formation();
            formation.setId(rs.getInt("id"));
            formation.setTitle(rs.getString("title"));
            formation.setDescription(rs.getString("description"));
            formation.setImage(rs.getString("image"));
            formation.setIs_online(rs.getBoolean("is_online"));
            formation.setPlace(rs.getString("place"));
            formation.setStart_date(rs.getTimestamp("start_date"));
            if(rs.getTimestamp("end_date") != null){
                formation.setEnd_date(rs.getTimestamp("end_date"));
            }else{
                formation.setEnd_date(null);
            }
            Formateur formateur = new Formateur();
            formateur.setFirstName(rs.getString("first_name"));
            formateur.setLastName(rs.getString("last_name"));
            formation.setFormateur(formateur);
            formations.add(formation);
        }
        return formations;
    }

}
