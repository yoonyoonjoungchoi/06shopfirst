package com.model2.mvc.service.product.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

import oracle.net.ano.Service;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration	(locations = {	"classpath:config/context-common.xml",
													"classpath:config/context-aspect.xml",
													"classpath:config/context-mybatis.xml",
													"classpath:config/context-transaction.xml" })
public class ProductServiceTest {

	//==>@RunWith,@ContextConfiguration 이용 Wiring, Test 할 instance DI
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	//@Test
	public void testAddProduct() throws Exception {     //새로 추가한 product가 잘 추가되었는지 확인하는 것임. 
				
		Product product=new Product();
		product.setProdName("aaa");
		product.setProdDetail("소니 헤드폰");
		product.setManufactureDay("20130514");
		product.setPrice(1000000);
		product.setImageFile("A.jpg");
		
		productService.addProduct(product);
		
		product = productService.getProduct(10013);

		//==> console 확인
		System.out.println(product);
		
		//==> API 확인
		Assert.assertEquals("aaa", product.getProdName());
		Assert.assertEquals("소니 헤드폰", product.getProdDetail());
		Assert.assertEquals("20130514", product.getManufactureDay());
		Assert.assertEquals(1000000, product.getPrice());
		Assert.assertEquals("A.jpg", product.getImageFile());
	}	
		
	
	
	//@Test
	public void testGetProduct() throws Exception {
	 
		Product product = new Product();
		
		product = productService.getProduct(10013);

		//==> console 확인
		//System.out.println(product);
		
		//==> API 확인
		Assert.assertEquals("aaa", product.getProdName());
		Assert.assertEquals("소니 헤드폰", product.getProdDetail());
		Assert.assertEquals("20130514", product.getManufactureDay());
		Assert.assertEquals(1000000, product.getPrice());
		Assert.assertEquals("A.jpg", product.getImageFile());

		
		Assert.assertNotNull(productService.getProduct(10002));
		Assert.assertNotNull(productService.getProduct(10003));
	}	
		
	//@Test
	 public void testUpdateProduct() throws Exception{     //이 부분 주석을 풀 때마다 database cmd가 update 되고, prodNo 시퀀스가 바뀜. 따라서 테스트 전에 cmd에서 prodNo 시퀀스 확인해야 함. 그리고 확인하고자 하는 prodNo를 맞춰서 test에 넣어줘야 함. 
		 
		Product product = productService.getProduct(10013);
		Assert.assertNotNull(product);
		
		Assert.assertEquals("aaa", product.getProdName());
		Assert.assertEquals("소니 헤드폰", product.getProdDetail());
		Assert.assertEquals("20130514", product.getManufactureDay());
		Assert.assertEquals(1000000, product.getPrice());
		Assert.assertEquals("A.jpg", product.getImageFile());
		
		product.setProdName("change");
		product.setProdDetail("change");
		product.setManufactureDay("change");
		product.setPrice(7777777);
		product.setImageFile("change");
	
		
		productService.updateProduct(product);
		
		product = productService.getProduct(10013);
		Assert.assertNotNull(product);
		
		//==> console 확인
		//System.out.println(product);
			
		//==> API 확인
		Assert.assertEquals("change", product.getProdName());
		Assert.assertEquals("change", product.getProdDetail());
		Assert.assertEquals("change", product.getManufactureDay());
		Assert.assertEquals(7777777, product.getPrice());
		Assert.assertEquals("change", product.getImageFile());
	 }	
	
	 //==>  주석을 풀고 실행하면....
	 //@Test
	 public void testGetProductListAll() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console 확인
	 	//System.out.println(list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword("");
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
	 	//==> console 확인
	 	//System.out.println(list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }
	 
	 //@Test
	 public void testGetProductListByProdNo() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword("10022");
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console 확인
	 	System.out.println(list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console 확인
	 	System.out.println(list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }
	 
	 //@Test
	 public void testGetProductListByProdName() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword("자전거");
	 	Map<String,Object> map = productService.getProductList(search);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(1, list.size()); //lib : hamcrest 
	 	
		//==> console 확인
	 	System.out.println(list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = productService.getProductList(search);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console 확인
	 	System.out.println(list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }	 
	 
}