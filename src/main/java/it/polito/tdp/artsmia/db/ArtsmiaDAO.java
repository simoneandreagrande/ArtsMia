package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.edgeModel;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public Integer getWeight(int sourceID, int targetID) {
		
//		String sql = "SELECT e1.`object_id`AS o1, e2.`object_id`AS o1, COUNT(*) as peso "
//				+ "FROM `exhibition_objects` e1, `exhibition_objects` e2 "
//				+ "WHERE e1.`exhibition_id`= e2.`exhibition_id` AND e1.`object_id`= ? AND e2.`object_id`= ?";
				
		String sql = "SELECT e1.`object_id`AS o1, e2.`object_id`AS o1, COUNT(*) as peso "
					+ "FROM `exhibition_objects` e1, `exhibition_objects` e2 "
					+ "WHERE e1.`exhibition_id`= e2.`exhibition_id` "
					+ "AND e1.`object_id`> e2.`object_id` "
					+ "GROUP BY e1.`object_id`, e2.`object_id` "
					+ "ORDER BY peso DESC";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, sourceID);
			st.setInt(2, targetID);
			ResultSet res = st.executeQuery();
			
			
			res.next();
			int peso = res.getInt("peso");

			res.close();
			conn.close();
			return peso;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public List<edgeModel> getAllWeights (Map<Integer, ArtObject> idMap) {
		
		String sql = "SELECT e1.`object_id` AS o2, e2.`object_id` AS o1, COUNT(*) as peso "
				+ "FROM `exhibition_objects` e1, `exhibition_objects` e2 "
				+ "WHERE e1.`exhibition_id` = e2.`exhibition_id` "
				+ "AND e1.`object_id` > e2.`object_id` "
				+ "GROUP BY e1.`object_id`, e2.`object_id` "
				+ "ORDER BY peso DESC";
		
		
		List<edgeModel> allEdges = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
	
		// in una sola interazione, ci dà tutti gli archi
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			
			
			while(res.next()) {
				int idSource = res.getInt("o1");
				int idTarget = res.getInt("o2");
				int peso = res.getInt("peso");
				
				edgeModel edgeI = new edgeModel(idMap.get(idSource), idMap.get(idTarget), peso);
				
				allEdges.add(edgeI);
				
				
			}	
			res.close();
			conn.close();
			return allEdges;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	


		
	}
}
