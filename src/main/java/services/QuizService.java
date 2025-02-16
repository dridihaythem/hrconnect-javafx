package services;

import models.Quiz;
import utils.MyDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class QuizService implements Crud<Quiz> {

    Connection conn;

    public QuizService(){
        this.conn = MyDb.getInstance().getConn();
    }

    @Override
    public void create(Quiz obj) throws Exception {
        String sql = "insert into quiz (formation_id,question,type,reponse1,reponse2,reponse3) VALUES (?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1,obj.getFormation_id());
        stmt.setString(2, obj.getQuestion());
        stmt.setString(3, obj.getType().toString());
        stmt.setString(4, obj.getReponse1());
        if(obj.getReponse2().isEmpty() || obj.getReponse2().isBlank()){
            stmt.setString(5, null);
        }else{
            stmt.setString(5, obj.getReponse2());
        }
        if(obj.getReponse3().isEmpty() || obj.getReponse3().isBlank()){
            stmt.setString(6, null);
        }else{
            stmt.setString(6, obj.getReponse3());
        }
        stmt.executeUpdate();
    }

    @Override
    public void update(Quiz obj) throws Exception {

    }

    @Override
    public void delete(Quiz obj) throws Exception {

    }

    @Override
    public List<Quiz> getAll() throws Exception {
        return List.of();
    }
}
