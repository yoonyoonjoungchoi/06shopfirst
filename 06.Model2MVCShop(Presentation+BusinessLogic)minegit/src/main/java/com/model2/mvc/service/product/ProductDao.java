package com.model2.mvc.service.product;


import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;

public interface ProductDao {
	
	// INSERT
	public int addProduct(Product product) throws Exception ;

	// SELECT ONE
	public Product getProduct(int prodNo) throws Exception ;

	// SELECT LIST
	public List<Product> getProductList(Search search) throws Exception ;

	// UPDATE
	public int updateProduct(Product product) throws Exception ;
	
	// 게시판 Page 처리를 위한 전체Row(totalCount)  return
	public int getTotalCount(Search search) throws Exception ;
	
}

	
	
	
	
	/*
	public void insertProduct(Product product) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "insert into PRODUCT values (seq_product_prod_no.nextval,?,?,?,?,?, sysdate)";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		
		pStmt.setString(1, product.getProdName());
		pStmt.setString(2, product.getProdDetail());
		pStmt.setString(3, product.getManufactureDay());
		pStmt.setInt(4, product.getPrice());
		pStmt.setString(5, product.getImageFile());
		pStmt.executeUpdate();
		
		pStmt.close();
		con.close();
	}

	public Product findProduct(int prodNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from PRODUCT where PROD_NO=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, prodNo);

		ResultSet rs = pStmt.executeQuery();

		Product product = null;
		
		while (rs.next()) {
			product = new Product();
			product.setProdNo(rs.getInt("PROD_NO"));
			product.setProdName(rs.getString("PROD_NAME"));
			product.setProdDetail(rs.getString("PROD_DETAIL"));
			product.setManufactureDay(rs.getString("MANUFACTURE_DAY"));
			product.setPrice(rs.getInt("PRICE"));
			product.setImageFile(rs.getString("IMAGE_FILE"));
			product.setRegDate(rs.getDate("REG_DATE"));
			
		}
		
		rs.close();
		pStmt.close();
		con.close();

		return	 product;
	}

	public Map<String,Object> getProductList(Search search) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "select * from PRODUCT";
		
		if (search.getSearchCondition() != null ) {
			if (search.getSearchCondition().equals("0")) {
				sql += " where PROD_NO LIKE'%" + search.getSearchKeyword()
						+ "%'";
			} else if (search.getSearchCondition().equals("1")) {
				sql += " where PROD_NAME LIKE'%" + search.getSearchKeyword()
						+ "%'";
			}
		}
		sql += " order by PROD_NO";
		
		System.out.println("ProductDAO::Original SQL :: " + sql);

		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount  :: " + totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
	
		System.out.println(search);
		
		
		List<Product> list = new ArrayList<Product>();
		
		while(rs.next()){
			Product product = new Product();
			product.setProdNo(rs.getInt("prod_no"));//애네만 고치면 됨
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			
			list.add(product);
		}
		
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);

		rs.close();
		pStmt.close();
		con.close();

		return map;
	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		PreparedStatement stmt = 
//			con.prepareStatement(	sql,
//														ResultSet.TYPE_SCROLL_INSENSITIVE,
//														ResultSet.CONCUR_UPDATABLE);
//		ResultSet rs = stmt.executeQuery();
//
//		rs.last();
//		int total = rs.getRow();
//		System.out.println("로우 수:" + total);
//
//		Map<String,Object> map2 = new HashMap<String,Object>();
//		map.put("count", new Integer(total));
//
//		rs.absolute(search.getCurrentPage() * search.getPageSize() - search.getPageSize()+1);
//		System.out.println("search.getCurrentPage():" + search.getCurrentPage());
//		System.out.println("search.getPageSize():" + search.getPageSize());
//
//		ArrayList<Product> list = new ArrayList<Product>();
//		if (total > 0) {
//			for (int i = 0; i < search.getPageSize(); i++) {
//				Product vo = new Product();
//					vo.setProdNo(rs.getInt("PROD_NO"));
//					vo.setProdName(rs.getString("PROD_NAME"));
//					vo.setProdDetail(rs.getString("PROD_DETAIL"));
//					vo.setManufactureDay(rs.getString("MANUFACTURE_DAY"));
//					vo.setPrice(rs.getInt("PRICE"));
//					vo.setImageFile(rs.getString("IMAGE_FILE"));
//					vo.setRegDate(rs.getDate("REG_DATE"));
//					System.out.println("list test :"+ list);
//					list.add(vo);
//					if (!rs.next())
//						break;
// 			}
//		}
//		System.out.println("list.size() : "+ list.size());
//		map.put("list", list);
//		System.out.println("map().size() : "+ map.size());
//           
//		con.close();
//			
//		return map;
//	}

	public void updateProduct(Product vo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "update PRODUCT set PROD_NAME=?, PROD_DETAIL=?,MANUFACTURE_DAY=?, PRICE=? where PROD_NO=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, vo.getProdName());
		pStmt.setString(2, vo.getProdDetail());
		pStmt.setString(3, vo.getManufactureDay());
		pStmt.setInt(4, vo.getPrice());
		pStmt.setString(5, vo.getImageFile());
		pStmt.executeUpdate();
		
		pStmt.close();
		con.close();
	}
	
	// 게시판 Page 처리를 위한 전체 Row(totalCount)  return
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	// 게시판 currentPage Row 만  return 
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
					"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
									" 	FROM (	"+sql+" ) inner_table "+
									"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("ProductDAO :: make SQL :: "+ sql);	
		
		return sql;
	}
}
*/