package org.openxava.test.tests;

import org.apache.commons.logging.*;
import org.openxava.tests.*;
import org.openxava.util.*;

import junit.framework.*;

/**
 * This suite test for bugs and features of OpenXava.
 * 
 * This suite does not include pretty layout. Therefore,
 * when you touch layout logic you have to test visually
 * at least the next cases:
 * - DeliveryFullInvoice:
 * 		With <reference-view reference="invoice" frame="true"/>
 * 		and  <reference-view reference="invoice" frame="false"/>
 * 		in FullInvoice view of Delivery   
 * 		The problem may be in layout of bottom element (outside invoice reference)
 * - Customer: seller with relationWithSeller group
 * - Invoice: Inside Liferay, buttons inside portlets
 * - A section with only a property (enmarcable and not enmarcable) inside Liferay
 * 		with more portlets below. The frame of the OX porltet mustn't wrap all other portlets.
 * 
 * @author Javier Paniza
 */
public class XavaSuite extends TestSuite {
	
	private static Log log = LogFactory.getLog(XavaSuite.class);
	
	public static void main(String [] argv) {
		String [] tests = {
			XavaSuite.class.getName()
		};
		junit.swingui.TestRunner.main(tests);	
	}
	
	public XavaSuite() {
		super();
	}
	public XavaSuite(Class theClass) {
		super(theClass);
	}
	public XavaSuite(String name) {
		super(name);
	}
	public static Test suite() {		
		TestSuite suite = new TestSuite();		
		suite.addTest(new TestSuite(WarehouseTest.class));		
		suite.addTest(new TestSuite(AJAXTest.class));
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(AnnotatedPOJOTest.class));
		}
		suite.addTest(new TestSuite(AverageSpeedTest.class));
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(BlogTest.class));
		}		
		suite.addTest(new TestSuite(CarrierFellowsNamesTest.class));
		suite.addTest(new TestSuite(CarrierTest.class));
		suite.addTest(new TestSuite(CarrierWithCalculatedFellowsTest.class));
		suite.addTest(new TestSuite(CarrierWithReadOnlyCalculatedFellowsTest.class));
		suite.addTest(new TestSuite(CarrierWithSectionsTest.class));
		suite.addTest(new TestSuite(CarrierWithSpecialSearchTest.class));		
		suite.addTest(new TestSuite(ChangeProductsPrice2Test.class));
		suite.addTest(new TestSuite(ChangeProductsPriceTest.class));
		suite.addTest(new TestSuite(ClerkTest.class));
		if (!XavaPreferences.getInstance().isEJB2Persistence()) {			
			suite.addTest(new TestSuite(ColorOnlyPOJOTest.class));
		}
		suite.addTest(new TestSuite(ColorTest.class));
		suite.addTest(new TestSuite(ColorM1Test.class));
		suite.addTest(new TestSuite(ColorWithGroupsAndChangeControllersTest.class));
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(CompositeTest.class));
		}		
		suite.addTest(new TestSuite(CustomerContactPersonAsAggregate3LevelsTest.class));
		suite.addTest(new TestSuite(CustomerContactPersonTest.class));
		suite.addTest(new TestSuite(CustomerNewOnInitInheritedTest.class));
		suite.addTest(new TestSuite(CustomerNewOnInitTest.class));
		suite.addTest(new TestSuite(CustomerRadioButtonTest.class));
		suite.addTest(new TestSuite(CustomerReadOnlyTest.class));
		suite.addTest(new TestSuite(CustomerSellerAsAggregateTest.class));
		suite.addTest(new TestSuite(CustomerSimpleTest.class));
		suite.addTest(new TestSuite(CustomerSomeMembersReadOnlyTest.class));
		suite.addTest(new TestSuite(CustomerTest.class));
		suite.addTest(new TestSuite(CustomerTwoSellersInListTest.class));		
		suite.addTest(new TestSuite(CustomerTwoSellersNumberInListTest.class));		
		suite.addTest(new TestSuite(CustomerWithSectionTest.class));				
		suite.addTest(new TestSuite(DeliveryGroupsInSectionsTest.class));
		suite.addTest(new TestSuite(DeliveryRemarks2002Test.class));		
		suite.addTest(new TestSuite(DeliveryTest.class));		
		suite.addTest(new TestSuite(DeliveryTypeJSPTest.class));
		suite.addTest(new TestSuite(DeliveryTypeTest.class));
		if (ModuleTestBase.isPortalEnabled()) {
			suite.addTest(new TestSuite(DescriptionTest.class));
		}		
		suite.addTest(new TestSuite(DriverTest.class));		
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(DrivingLicenceTest.class));
		}		
		try {
			Class ejbTestClass = Class.forName("org.openxava.test.tests.EJBTest");
			if (XavaPreferences.getInstance().isEJB2Persistence()) {
				suite.addTest(new TestSuite(ejbTestClass));
			}
		}
		catch (ClassNotFoundException ex) {
			log.warn("EJBTest does not found in classpath, test will not be executed");
		}		
		suite.addTest(new TestSuite(FamilyListOnlyTest.class));		
		suite.addTest(new TestSuite(FamilyTest.class));		
		suite.addTest(new TestSuite(FamilyWithInheritanceControllerTest.class));
		suite.addTest(new TestSuite(FamilyProductsReportTest.class));
		suite.addTest(new TestSuite(FamilyRangeProductsReportTest.class));
		suite.addTest(new TestSuite(FamilyXProductsReportTest.class));		
		suite.addTest(new TestSuite(FormulaTest.class));				
		if (!ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(HibernateTest.class));
		}
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(HumanTest.class));
		}				
		suite.addTest(new TestSuite(Invoice20020001Test.class));
		suite.addTest(new TestSuite(Invoice2002Test.class));		
		suite.addTest(new TestSuite(Invoice2004Test.class));
		suite.addTest(new TestSuite(Invoice2Test.class));		
		suite.addTest(new TestSuite(InvoiceActiveYearTest.class));		
		suite.addTest(new TestSuite(InvoiceCustomerAsAggregateTest.class));		
		suite.addTest(new TestSuite(InvoiceDeliveriesTest.class));
		suite.addTest(new TestSuite(InvoiceFromCustomersTest.class));
		suite.addTest(new TestSuite(InvoiceFromDeliveriesTest.class));		
		suite.addTest(new TestSuite(InvoiceNestedSectionsTest.class));
		suite.addTest(new TestSuite(InvoiceNoListTest.class));		
		suite.addTest(new TestSuite(InvoiceTest.class));	
		suite.addTest(new TestSuite(InvoiceWithParameterInURLTest.class));
		if (!XavaPreferences.getInstance().isEJB2Persistence()) {
			suite.addTest(new TestSuite(IssueTest.class));
		}								
		suite.addTest(new TestSuite(IsTest.class));
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(JavaProgrammerExtendedViewsTest.class));
			suite.addTest(new TestSuite(JavaProgrammerTest.class));
		}						
		suite.addTest(new TestSuite(JourneyTest.class));
		if (XavaPreferences.getInstance().isJPAPersistence()) {			
			suite.addTest(new TestSuite(JPATest.class));			
		}
		suite.addTest(new TestSuite(MapFacadeTest.class));
		suite.addTest(new TestSuite(NotZeroValidatorTest.class));
		suite.addTest(new TestSuite(Office2Test.class));
		suite.addTest(new TestSuite(OfficeOnlyWarehouseTest.class));
		suite.addTest(new TestSuite(OfficeTest.class));
		suite.addTest(new TestSuite(OnlyEditDetailsInvoiceTest.class));
		suite.addTest(new TestSuite(OnlyReadDetailsInvoiceTest.class));
		suite.addTest(new TestSuite(OrderTest.class));
		suite.addTest(new TestSuite(POJOTest.class));
		suite.addTest(new TestSuite(PositiveValidatorTest.class));
		suite.addTest(new TestSuite(Product2ColorWithFrameTest.class));
		suite.addTest(new TestSuite(Product2OnlySoftwareTest.class));
		suite.addTest(new TestSuite(Product2ReferenceAndStereotypeTest.class));		
		suite.addTest(new TestSuite(Product2Test.class));
		suite.addTest(new TestSuite(Product2WithFormulaAsAggregateTest.class));
		suite.addTest(new TestSuite(Product2WithFormulaTest.class));		
		suite.addTest(new TestSuite(Product3ChangeActionsOnSearchTest.class));
		suite.addTest(new TestSuite(Product3Test.class));				
		suite.addTest(new TestSuite(Product3WithDescriptionsListTest.class));		
		suite.addTest(new TestSuite(Product3WithGroupTest.class));
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(Product4Test.class));
		}
		suite.addTest(new TestSuite(ProductTest.class));
		suite.addTest(new TestSuite(ProductWithSectionTest.class));		
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(ProgrammerTest.class));
		}							
		suite.addTest(new TestSuite(PublicJSPTest.class));
		suite.addTest(new TestSuite(QuarterTest.class));
		suite.addTest(new TestSuite(ReportTest.class));
		suite.addTest(new TestSuite(SellerCannotCreateCustomerTest.class));
		suite.addTest(new TestSuite(SellerJSPTest.class));
		suite.addTest(new TestSuite(SellerTest.class));
		suite.addTest(new TestSuite(SellerWithCustomersAsAggregateTest.class));
		suite.addTest(new TestSuite(SellerWithDescriptionsListJSPTest.class));
		if (!XavaPreferences.getInstance().isEJB2Persistence()) {
			suite.addTest(new TestSuite(ServiceInvoiceTest.class));
			suite.addTest(new TestSuite(ServiceOnlyPOJOTest.class));
		}
		suite.addTest(new TestSuite(ServiceTest.class));
		suite.addTest(new TestSuite(SeveralModulesTest.class));
		suite.addTest(new TestSuite(ShipmentChargeTest.class));
		suite.addTest(new TestSuite(ShipmentSeparatedTimeTest.class));
		suite.addTest(new TestSuite(ShipmentTest.class));				
		if (!XavaPreferences.getInstance().isEJB2Persistence()) {
			suite.addTest(new TestSuite(SizeTest.class));
		}		
		suite.addTest(new TestSuite(StateHibernateTest.class));
		if (XavaPreferences.getInstance().isJPAPersistence()) {
			// This test can work with any persistence provider, because it uses direct JPA APIs,
			// but asking 'isJPAPersistence' we are sure that Java 5 is used in server
			suite.addTest(new TestSuite(StateJPATest.class));
		}
		suite.addTest(new TestSuite(Subfamily2Test.class));
		suite.addTest(new TestSuite(SubfamilySelectTest.class));	
		suite.addTest(new TestSuite(SubfamilyTest.class));
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(SummerCourseTest.class));
		}
		if (ModuleTestBase.isPortalEnabled()) {
			suite.addTest(new TestSuite(TaskTest.class));
		}	
		suite.addTest(new TestSuite(TestServletTest.class));
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(ToDoListTest.class));
		}
		suite.addTest(new TestSuite(TransportCharge2Test.class));		
		suite.addTest(new TestSuite(TransportChargeTest.class));
		suite.addTest(new TestSuite(TransportChargeWithDistanceTest.class));		
		if (ModuleTestBase.isOX3()) {
			suite.addTest(new TestSuite(Warehouse2Test.class));
		}
		suite.addTest(new TestSuite(WarehouseSpecialNewTest.class));
		
		return suite;
	}
	
}