package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO seller_ww " + "(Name, Email, BirthDate, BaseSalary, Department_Id) "
					+ "VALUES " + "(?, ?, ?, ?, ?)");
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartmment().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				// Banco Oracle não retorna o ultimo id vou fazer manualmente
				PreparedStatement st1 = null;
				ResultSet rs = null;

				st1 = conn.prepareStatement("SELECT MAX(id) as id FROM Seller_ww");
				rs = st1.executeQuery();

				if (rs.next()) {
					int id = rs.getInt("id");
					System.out.println("Key Generated: " + id);
					obj.setId(id);
				}
				DB.closeStatement(st1);
				DB.closeResultSet(rs);

			} else {
				DecrementSequenceSeller();
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			DecrementSequenceSeller();
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					 "UPDATE seller_ww "
					+"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, Department_Id = ? "
					+"WHERE Id = ?"
					);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartmment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			DecrementSequenceSeller();
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {

	}

	private void DecrementSequenceSeller() {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("ALTER SEQUENCE SEQ_id_Seller_ww " + "INCREMENT BY -1 "

					+ "SELECT SEQ_id_Seller_ww.nextval " + "FROM dual "

					+ "ALTER SEQUENCE SEQ_id_Seller_ww " + "INCREMENT BY 1");
		} catch (SQLException e) {
			throw new DbException("Error in decrement Sequence!");
		}

		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT " + "seller.*, " + "department.Name as DepName "
					+ "FROM seller_ww seller, department_ww department " + "WHERE seller.department_id = department.id "
					+ "    and seller.id = ? "

			);

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {

				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);

				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("id"));
		obj.setName(rs.getString("name"));
		obj.setEmail(rs.getString("email"));
		obj.setBirthDate(rs.getDate("birthDate"));
		obj.setBaseSalary(rs.getDouble("baseSalary"));
		obj.setDepartmment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("department_id"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " + "FROM seller_ww seller, department_ww department "
							+ "WHERE seller.Department_Id = department.Id " + "ORDER BY department.Name");

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("department_id"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("department_id"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
					+ "FROM seller_ww seller, department_ww department " + "WHERE seller.Department_Id = department.Id "
					+ "AND Department_Id = ? " + "ORDER BY department.Name");

			st.setInt(1, department.getId());
			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("department_id"));

				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("department_id"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
