/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurant.manager.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import config.jdbcConfig;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import restaurant.manager.models.HeThong;
import static util.MD5Library.md5;


/**
 * FXML Controller class
 *
 * @author sky
 */
public class HeThongController implements Initializable {

    @FXML
    private JFXTextField txtTenNhanVien;
    @FXML
    private JFXTextField txtMatKhau;
    @FXML
    private JFXTextField txtTimKiem;
    @FXML
    private JFXTextField txtMaNhanVien;
    @FXML
    private JFXButton btnThem;
    @FXML
    private JFXButton btnXoa;
    @FXML
    private JFXButton btnSua;
    @FXML
    private TableColumn<HeThong, String> tblColMaNhanVien;
    @FXML
    private TableColumn<HeThong, String> tblColTenNhanVien;
    @FXML
    private TableColumn<HeThong, String> tblColTenDangNhap;
    @FXML
    private TableColumn<HeThong, String> tblColMatKhau;
    @FXML
    private TableColumn<HeThong, String> tblColChucVu;
    @FXML
    private TableView<HeThong> tblHeThong;
    private ObservableList<HeThong> listHeThong;
    private FilteredList<HeThong> filteredData;
    
    
    private ObservableList<HeThong> getHeThong() throws SQLException {
        String sql = "SELECT ht.manhanvien, nv.tennhanvien, ht.username ,"
                                + "ht.password, nv.chucvu "
                                + "FROM hethong as  ht , nhanvien as nv "
                                + "WHERE ht.manhanvien = nv.manhanvien";
        PreparedStatement p = jdbcConfig.connection.prepareStatement(sql);

        ResultSet r = jdbcConfig.ExecuteQuery(p);//Thực thi câu truy vấn
        listHeThong = FXCollections.observableArrayList();
        filteredData=new FilteredList<>(listHeThong,e->true);
        while (r.next()) {
            listHeThong.add(new HeThong(r.getString(1),r.getString(2),
                       r.getString(3),r.getString(4),r.getString(5)));                  
        }
        p.close();
	r.close();
        return listHeThong;
    }    

    private void setTableHeThong(ObservableList e) throws SQLException {
        tblHeThong.setItems(e);//Lấy giá trị DB rồi set cho bảng   
        tblColMaNhanVien.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        tblColTenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        tblColTenDangNhap.setCellValueFactory(new PropertyValueFactory<>("tenDangNhap"));
        tblColMatKhau.setCellValueFactory(new PropertyValueFactory<>("matKhau"));
        tblColChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
    }
    private void insertHeThong() throws SQLException {
        String sql = String.format("INSERT INTO hethong(username, manhanvien,password)"
                + "VALUES (?, ?, ?)");

        PreparedStatement p = jdbcConfig.connection.prepareStatement(sql);
        p.setString(1, txtTenNhanVien.getText());
        p.setString(2, txtMaNhanVien.getText());
        p.setString(3, md5(txtMatKhau.getText()));
        int rows = jdbcConfig.ExecuteUpdateQuery(p);
        if (rows != 0) {
            setTableHeThong(getHeThong());
        }
    }
    private void updateHeThong() throws SQLException {
        String sql = String.format("UPDATE hethong SET \n"
                + "manhanvien = ?,\n"
                + "password = ?\n"
                + "WHERE username = ? ");
        PreparedStatement p = jdbcConfig.connection.prepareStatement(sql);
        p.setString(1, txtMaNhanVien.getText());
        p.setString(2, md5(txtMatKhau.getText()));
        p.setString(3, txtTenNhanVien.getText());
        int row = p.executeUpdate();
        if (row == 1) {
            setTableHeThong(getHeThong());
        }
    }
    private void deleteHeThong() throws SQLException {
        String sql = String.format("DELETE FROM hethong\n"
                + "WHERE username = ?");
        PreparedStatement p = jdbcConfig.connection.prepareStatement(sql);
        p.setString(1, txtTenNhanVien.getText());
        int row = p.executeUpdate();
        if (row == 1) {
            setTableHeThong(getHeThong());
        }
    }
    
    
    @FXML
    private void searchHeThong()
    {
        txtTimKiem.textProperty().addListener((observableValue,oldValue,newValue)->{
		filteredData.setPredicate((Predicate<? super HeThong>)HeThong->{
			if(newValue==null||newValue.isEmpty()){
				return true;
			}
			String lowerCaseFilter=newValue.toLowerCase();
			if(HeThong.getMaNhanVien().toLowerCase().contains(lowerCaseFilter)){
				return true;
			}
                        else if(HeThong.getTenDangNhap().toLowerCase().contains(lowerCaseFilter)){
				return true;
                        }
                        else if(HeThong.getTenNhanVien().toLowerCase().contains(lowerCaseFilter)){
				return true;
                        }                    
			return false;
		});
	});
	SortedList<HeThong> sortedData=new SortedList<>(filteredData);
	sortedData.comparatorProperty().bind(tblHeThong.comparatorProperty());
	tblHeThong.setItems(sortedData);
    }  
    
    @FXML
    private void SelectRow(MouseEvent e) throws SQLException {
        if (e.getClickCount() == 1) {
            txtMaNhanVien.setText(tblHeThong.getSelectionModel()
                    .getSelectedItem().getMaNhanVien());
            txtTenNhanVien.setText(tblHeThong.getSelectionModel()
                    .getSelectedItem().getTenDangNhap());
            txtMatKhau.setText(tblHeThong.getSelectionModel()
                    .getSelectedItem().getMatKhau());   
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            jdbcConfig.Connect();
            setTableHeThong(getHeThong()); 
            searchHeThong();
            btnThem.setOnAction(e->{
                try {
                    insertHeThong();
                } catch (SQLException ex) {
                    Logger.getLogger(HeThongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            btnXoa.setOnAction(e->{
                try {
                    deleteHeThong();
                } catch (SQLException ex) {
                    Logger.getLogger(HeThongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            btnSua.setOnAction(e->{
                try {
                    updateHeThong();
                } catch (SQLException ex) {
                    Logger.getLogger(HeThongController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
           
        } catch (SQLException ex) {
            Logger.getLogger(HeThongController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}