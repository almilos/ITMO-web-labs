package ru.ifmo.web.client;

import lombok.SneakyThrows;
import org.apache.juddi.api_v3.AccessPointType;
import org.uddi.api_v3.AccessPoint;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.ServiceDetail;

import javax.xml.ws.BindingProvider;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Client {
  private static JUDDIClient juddiClient;
  private static MetallicaService metallicaPort;
  public static void main( String... args ) throws IOException {
    URL url = new URL( "http://localhost:8081/metallica?wsdl" );
    Metallica_Service metallicaService = new Metallica_Service( );
    metallicaPort = metallicaService.getMetallicaServicePort( );


    BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
    System.out.println("Enter JUDDI username");
    String username = reader.readLine().trim();
    System.out.println("Enter JUDDI user password");
    String password = reader.readLine().trim();
    juddiClient = new JUDDIClient("META-INF/uddi.xml");
    juddiClient.authenticate(username, password);

    int currentState = -1;

    Thread thr;
    while( true ) {
      switch( currentState ) {

        case -1:
          System.out.println( "\n1. Show all Metallica members" );
          System.out.println( "2. Search member" );
          System.out.println( "3. Add member" );
          System.out.println( "4. Update member" );
          System.out.println( "5. Delete member" );
          System.out.println( "6. List all businesses" );
          System.out.println( "7. Register a business" );
          System.out.println( "8. Register a service" );
          System.out.println( "9. Find and use a service" );
          System.out.println( "0. Exit" );
          currentState = readState( currentState, reader );
          break;

        case 1:
          System.out.println( "Found:" );
          thr = new Thread( new Runnable() 
          {
            public void run() {
              try {
                metallicaPort.findAll( ).stream( ).map( Client::metallicaToString ).forEach( System.out::println );
              }
              catch( MetallicaServiceException ex ) {
                System.out.println( ex.getFaultInfo( ) );
              }
            }
          });
          thr.start( );

          currentState = -1;
          break;

        case 2:
          System.out.println("id:");
          Long id = readLong( reader );

          System.out.println( "name:" );
          String name = readString( reader );

          System.out.println( "instrument:" );
          String instrument = readString( reader );

          System.out.println( "entrydate(yyyy-mm-dd):" );
          XMLGregorianCalendar entrydate = readDate( reader );

          System.out.println( "networth:" );
          Integer networth = readInteger( reader );

          System.out.println( "birthdate(yyyy-mm-dd):" );
          XMLGregorianCalendar birthdate = readDate( reader );

          System.out.println( "Found:" );

          thr = new Thread( new Runnable() 
          {
            public void run() {
              try {
                metallicaPort.findWithFilters( id, name, instrument, entrydate, networth, birthdate ).stream( ).map( Client::metallicaToString ).forEach( System.out::println );
              }
              catch( MetallicaServiceException ex ) {
                System.out.println( ex.getFaultInfo( ) );
              }
            }
          });
          thr.start( );

          currentState = -1;

          break;

        case 3:
          System.out.println( "name:" );
          String c_name = readString( reader );
          if( c_name == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }
          
          System.out.println( "instrument:" );
          String c_instrument = readString( reader );
          if( c_instrument == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }

          System.out.println( "entrydate(yyyy-mm-dd):" );
          XMLGregorianCalendar c_entrydate = readDate( reader );
          if( c_entrydate == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }

          System.out.println( "networth:" );
          Integer c_networth = readInteger( reader );
          if( c_networth == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }

          System.out.println( "birthdate(yyyy-mm-dd):" );
          XMLGregorianCalendar c_birthdate = readDate( reader );

          Long new_id = null;
          try {
            new_id = metallicaPort.create( c_name, c_instrument, c_entrydate, c_networth, c_birthdate );
          }
          catch( MetallicaServiceException ex ) {
            System.out.println( ex.getFaultInfo( ) );
          } 
          if( new_id != 0 ) {
            System.out.println( "New ID " + new_id.toString( ) );
          }

          currentState = -1;
        break;

        case 4:
          System.out.println( "id:" );
          Long u_id = readLong( reader );
          if( u_id == null ) {
            System.out.println( "Value is invalid or empty" );
            currentState = -1;
            break;
          }

          System.out.println( "name:" );
          String u_name = readString( reader );

          System.out.println( "instrument:" );
          String u_instrument = readString( reader );

          System.out.println( "entrydate(yyyy-mm-dd):" );
          XMLGregorianCalendar u_entrydate = readDate( reader );

          System.out.println( "networth:" );
          Integer u_networth = readInteger( reader );

          System.out.println( "birthdate(yyyy-mm-dd):" );
          XMLGregorianCalendar u_birthdate = readDate( reader );

          int u_ret = 0;
          try {
            u_ret = metallicaPort.update( u_id, u_name, u_instrument, u_entrydate, u_networth, u_birthdate );
          }
          catch( MetallicaServiceException ex ) {
            System.out.println( ex.getFaultInfo( ) );
          } 
          if( u_ret != 0 ) {
            System.out.println( "OK" );
          }

          currentState = -1;
          break;   

        case 5:
          System.out.println( "id:" );
          Long d_id = readLong( reader );

          int d_ret = 0;
          try {
            d_ret = metallicaPort.delete( d_id );
          } 
          catch( MetallicaServiceException ex ) {
            System.out.println( ex.getFaultInfo( ) );
          } 
          if( d_ret != 0 ) {
            System.out.println( "OK" );
          }

          currentState = -1;
          break;

        case 0:
          return;

        case 6:
          listBusinesses(null);
          currentState = -1;
          break;
        case 7:
          System.out.println("Enter business name");
          String bn = readString(reader);
          if (bn != null) {
            createBusiness(bn);
          }
          currentState = -1;
          break;
        case 8:
          listBusinesses(null);
          String bk;
          do {
            System.out.println("Enter business key");
            bk = readString(reader);
          } while (bk == null);

          String sn;
          do {
            System.out.println("Enter service name");
            sn = readString(reader);
          } while (sn == null);

          String surl;
          do {
            System.out.println("Enter wsdl link");
            surl = readString(reader);
          } while (surl == null);
          createService(bk, sn, surl);
          currentState = -1;
          break;
        case 9:
          System.out.println("Enter service name");
          String fsn = readString(reader);
          filterServices(fsn);
          System.out.println("Enter service key");
          String key = readString(reader);
          if (key != null) {
            useService(key);
          }
          currentState = -1;
          break;
        
        default:
          currentState = -1;
          break;
      }
    }
  }

  @SneakyThrows
    private static void useService(String serviceKey) {

        ServiceDetail serviceDetail = juddiClient.getService(serviceKey.trim());
        if (serviceDetail == null || serviceDetail.getBusinessService() == null || serviceDetail.getBusinessService().isEmpty()) {
            System.out.printf("Can not find service by key '%s'\b", serviceKey);
            return;
        }
        List<BusinessService> services = serviceDetail.getBusinessService();
        BusinessService businessService = services.get(0);
        BindingTemplates bindingTemplates = businessService.getBindingTemplates();
        if (bindingTemplates == null || bindingTemplates.getBindingTemplate().isEmpty()) {
            System.out.printf("No binding template found for service '%s' '%s'\n", serviceKey, businessService.getBusinessKey());
            return;
        }
        for (BindingTemplate bindingTemplate : bindingTemplates.getBindingTemplate()) {
            AccessPoint accessPoint = bindingTemplate.getAccessPoint();
            if (accessPoint.getUseType().equals(AccessPointType.END_POINT.toString())) {
                String value = accessPoint.getValue();
                System.out.printf("Use endpoint '%s'\n", value);
                changeEndpointUrl(value);
                return;
            }
        }
        System.out.printf("No endpoint found for service '%s'\n", serviceKey);
    }

    @SneakyThrows
    private static void createService(String businessKey, String serviceName, String wsdlUrl) {
        List<ServiceDetail> serviceDetails = juddiClient.publishUrl(businessKey.trim(), serviceName.trim(), wsdlUrl.trim());
        System.out.printf("Services published from wsdl %s\n", wsdlUrl);
        JUDDIUtil.printServicesInfo(serviceDetails.stream()
                .map(ServiceDetail::getBusinessService)
                .flatMap(List::stream)
                .collect(Collectors.toList())
        );
    }

    @SneakyThrows
    public static void createBusiness(String businessName) {
        businessName = businessName.trim();
        BusinessDetail business = juddiClient.createBusiness(businessName);
        System.out.println("New business was created");
        for (BusinessEntity businessEntity : business.getBusinessEntity()) {
            System.out.printf("Key: '%s'\n", businessEntity.getBusinessKey());
            System.out.printf("Name: '%s'\n", businessEntity.getName().stream().map(Name::getValue).collect(Collectors.joining(" ")));
        }
    }

    public static void changeEndpointUrl(String endpointUrl) {
        ((BindingProvider) metallicaPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl.trim());
    }


    @SneakyThrows
    private static void filterServices(String filterArg) {
        List<BusinessService> services = juddiClient.getServices(filterArg);
        JUDDIUtil.printServicesInfo(services);
    }

    @SneakyThrows
    private static void listBusinesses(Void ignored) {
        JUDDIUtil.printBusinessInfo(juddiClient.getBusinessList().getBusinessInfos());
    }

  private static String readString( BufferedReader reader ) throws IOException {
    String trim = reader.readLine( ).trim( );
    if( trim.isEmpty( ) ) {
      return null;
    }
    return trim;
  }

  private static XMLGregorianCalendar readDate( BufferedReader reader ) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );

      Date rd = sdf.parse( reader.readLine( ) );
      XMLGregorianCalendar birthdate;

      GregorianCalendar c = new GregorianCalendar( );

      if( rd != null ) {
        c.setTime( rd );
        return DatatypeFactory.newInstance( ).newXMLGregorianCalendar( c );
      } else {
        return null;
      }
    } catch( java.lang.Exception e ) {
      return null;
    }
  }

  private static Long readLong( BufferedReader reader ) {
    try {
      return Long.parseLong( reader.readLine( ) );
    } catch( java.lang.Exception e ) {
      return null;
    }
  }

  private static Integer readInteger( BufferedReader reader ) {
    try {
      return Integer.parseInt( reader.readLine( ) );
    } catch( java.lang.Exception e ) {
      return null;
    }
  }

  private static int readState( int current, BufferedReader reader ) {
    try {
      return Integer.parseInt( reader.readLine( ) );
    } catch( java.lang.Exception e ) {
      return current;
    }
  }

  private static String metallicaToString( Metallica metallica ) {
    return metallica.getId( ) + ". " +
      metallica.getName( ) + ", " +
      "plays: " + metallica.getInstrument( ) + ", " + 
      "entered: " + metallica.getEntrydate( ) + ", " +
      "networth: " + metallica.getNetworth( ) + ", " +
      "birthdate: " + metallica.getBirthdate( );
  }

}
