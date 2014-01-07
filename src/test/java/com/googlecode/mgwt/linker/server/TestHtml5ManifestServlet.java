package com.googlecode.mgwt.linker.server;

import com.googlecode.mgwt.linker.server.propertyprovider.MgwtOsPropertyProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.realityforge.gwt.appcache.server.propertyprovider.PropertyProvider;
import static org.mockito.Mockito.*;

public class TestHtml5ManifestServlet
{
  static class TestManifestServlet
    extends Html5ManifestServletBase
  {
    private ServletContext _servletContext;

    @Override
    public ServletContext getServletContext()
    {
      if ( null == _servletContext )
      {
        _servletContext = mock( ServletContext.class );
      }
      return _servletContext;
    }

    protected final void addPropertyProviderForTest( final PropertyProvider propertyProvider )
    {
      addPropertyProvider( propertyProvider );
    }
  }

  @Test
  public void serve_withExactMatch()
    throws Exception
  {
    final TestManifestServlet servlet = new TestManifestServlet();

    final String strongPermutation = "C7D408F8EFA266A7F9A31209F8AA7446";

    final String userAgent = "safari";
    final String mobileUserAgent = "mobilesafari";
    final String os = MgwtOsPropertyProvider.iPhone.getValue();

    setProperties( servlet, userAgent, mobileUserAgent, os );
    setupPermutationsXML( servlet, strongPermutation, userAgent, mobileUserAgent, os );

    final String manifestContent = setupManifestFile( servlet, strongPermutation );

    final HttpServletResponse response = mock( HttpServletResponse.class );
    final ServletOutputStream output = mock( ServletOutputStream.class );
    when( response.getOutputStream() ).thenReturn( output );

    performRequest( servlet, response );

    verify( output ).write( manifestContent.getBytes( "US-ASCII" ) );
  }

  @Test
  public void serve_withMergeOfRetinaAndNonRetina()
    throws Exception
  {
    final TestManifestServlet servlet = new TestManifestServlet();

    final String permutation1 = "A";
    final String permutation2 = "B";

    final String userAgent = "safari";
    final String mobileUserAgent = "mobilesafari";

    setProperties( servlet, userAgent, mobileUserAgent, MgwtOsPropertyProvider.iPhone_undefined.getValue() );
    setupPermutationsXML( servlet,
                          permutation1, userAgent, mobileUserAgent, MgwtOsPropertyProvider.iPhone.getValue(),
                          permutation2, MgwtOsPropertyProvider.retina.getValue() );

    setupManifestFile( servlet, permutation1 );
    setupManifestFile( servlet, permutation2 );

    final HttpServletResponse response = mock( HttpServletResponse.class );
    final ServletOutputStream output = mock( ServletOutputStream.class );
    when( response.getOutputStream() ).thenReturn( output );

    performRequest( servlet, response );

    final String combinedManifest =
      "CACHE MANIFEST\n" +
      "\n" +
      "CACHE:\n" +
      permutation1 + ".txt\n" +
      permutation2 + ".txt\n" +
      "\n" +
      "\n" +
      "NETWORK:\n";
    verify( output ).write( combinedManifest.getBytes( "US-ASCII" ) );
  }

  @Test
  public void serve_withMissingRetina()
    throws Exception
  {
    final TestManifestServlet servlet = new TestManifestServlet();

    final String permutation1 = "A";

    final String userAgent = "safari";
    final String mobileUserAgent = "mobilesafari";

    setProperties( servlet, userAgent, mobileUserAgent, MgwtOsPropertyProvider.iPhone_undefined.getValue() );
    setupPermutationsXML( servlet,
                          permutation1,
                          userAgent,
                          mobileUserAgent,
                          MgwtOsPropertyProvider.iPhone.getValue() );

    setupManifestFile( servlet, permutation1 );

    final HttpServletResponse response = mock( HttpServletResponse.class );
    final ServletOutputStream output = mock( ServletOutputStream.class );
    when( response.getOutputStream() ).thenReturn( output );

    performRequest( servlet, response );

    verify( response ).sendError( HttpServletResponse.SC_NOT_FOUND );
  }

    @Test
  public void serve_withMissingNonRetina()
    throws Exception
  {
    final TestManifestServlet servlet = new TestManifestServlet();

    final String permutation1 = "A";

    final String userAgent = "safari";
    final String mobileUserAgent = "mobilesafari";

    setProperties( servlet, userAgent, mobileUserAgent, MgwtOsPropertyProvider.iPhone_undefined.getValue() );
    setupPermutationsXML( servlet,
                          permutation1,
                          userAgent,
                          mobileUserAgent,
                          MgwtOsPropertyProvider.retina.getValue() );

    setupManifestFile( servlet, permutation1 );

    final HttpServletResponse response = mock( HttpServletResponse.class );

    performRequest( servlet, response );

    verify( response ).sendError( HttpServletResponse.SC_NOT_FOUND );
  }

    @Test
  public void serve_withNoMatch()
    throws Exception
  {
    final TestManifestServlet servlet = new TestManifestServlet();

    setProperties( servlet, "safari", "mobilesafari", MgwtOsPropertyProvider.retina.getValue() );
    setupPermutationsXML( servlet, "X", "X", "X", "X" );

    setupManifestFile( servlet, "A" );

    final HttpServletResponse response = mock( HttpServletResponse.class );
    performRequest( servlet, response );
    verify( response ).sendError( HttpServletResponse.SC_NOT_FOUND );
  }

  private void performRequest( final TestManifestServlet servlet, final HttpServletResponse response )
    throws ServletException, IOException
  {
    final HttpServletRequest request = mock( HttpServletRequest.class );
    when( request.getServletPath() ).thenReturn( "/mymodule.appcache" );
    when( request.getMethod() ).thenReturn( "GET" );
    servlet.service( request, response );
  }

  private String setupManifestFile( final TestManifestServlet servlet, final String strongPermutation )
    throws IOException
  {
    final String manifestContent = "CACHE MANIFEST\n" + strongPermutation + ".txt\n";
    final File manifest = createFile( "manifest", "appcache", manifestContent );

    when( servlet.getServletContext().getRealPath( "/mymodule/" + strongPermutation + ".appcache" ) ).
      thenReturn( manifest.getAbsolutePath() );
    return manifestContent;
  }

  private void setupPermutationsXML( final TestManifestServlet servlet,
                                     final String strongPermutation,
                                     final String userAgent,
                                     final String mobileUserAgent,
                                     final String os )
    throws IOException
  {
    final String permutationContent =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
      "<permutations>\n" +
      "   <permutation name=\"" + strongPermutation + "\">\n" +
      "      <user.agent>" + userAgent + "</user.agent>\n" +
      "      <mobile.user.agent>" + mobileUserAgent + "</mobile.user.agent>\n" +
      "      <mgwt.os>" + os + "</mgwt.os>\n" +
      "   </permutation>\n" +
      "</permutations>\n";

    createPermutationsXML( servlet, permutationContent );
  }

  private void setupPermutationsXML( final TestManifestServlet servlet,
                                     final String permutation1,
                                     final String userAgent,
                                     final String mobileUserAgent,
                                     final String os1,
                                     final String permutation2,
                                     final String os2 )
    throws IOException
  {
    final String permutationContent =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
      "<permutations>\n" +
      "   <permutation name=\"" + permutation1 + "\">\n" +
      "      <user.agent>" + userAgent + "</user.agent>\n" +
      "      <mobile.user.agent>" + mobileUserAgent + "</mobile.user.agent>\n" +
      "      <mgwt.os>" + os1 + "</mgwt.os>\n" +
      "   </permutation>\n" +
      "   <permutation name=\"" + permutation2 + "\">\n" +
      "      <user.agent>" + userAgent + "</user.agent>\n" +
      "      <mobile.user.agent>" + mobileUserAgent + "</mobile.user.agent>\n" +
      "      <mgwt.os>" + os2 + "</mgwt.os>\n" +
      "   </permutation>\n" +
      "</permutations>\n";

    createPermutationsXML( servlet, permutationContent );
  }

  private void createPermutationsXML( final TestManifestServlet servlet, final String fileContent )
    throws IOException
  {
    final File file = createFile( "permutations", "xml", fileContent );
    when( servlet.getServletContext().getRealPath( "/mymodule/permutations.xml" ) ).
      thenReturn( file.getAbsolutePath() );
  }

  private void setProperties( final TestManifestServlet servlet,
                              final String userAgent,
                              final String mobileUserAgent,
                              final String os )
  {
    servlet.addPropertyProviderForTest( new TestPropertyProvider( "user.agent", userAgent ) );
    servlet.addPropertyProviderForTest( new TestPropertyProvider( "mobile.user.agent", mobileUserAgent ) );
    servlet.addPropertyProviderForTest( new TestPropertyProvider( "mgwt.os", os ) );
  }

  private File createFile( final String prefix, final String extension, final String content )
    throws IOException
  {
    final File file = File.createTempFile( prefix, extension );
    file.deleteOnExit();
    final FileOutputStream outputStream = new FileOutputStream( file );
    outputStream.write( content.getBytes() );
    outputStream.close();
    return file;
  }
/*
  @Override
  protected boolean handleUnmatchedRequest( final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final String moduleName,
                                            final String baseUrl,
                                            final List<BindingProperty> computedBindings )
    throws ServletException
  {
    //TODO: Add tests for this!
    final boolean isIPhoneWithoutCookie = computedBindings.contains( MgwtOsPropertyProvider.iPhone_undefined );
    final boolean isIPadWithoutCookie = computedBindings.contains( MgwtOsPropertyProvider.iPad_undefined );
    if ( isIPhoneWithoutCookie || isIPadWithoutCookie )
    {
      final List<BindingProperty> nonRetinaMatch = new ArrayList<BindingProperty>();
      final List<BindingProperty> retinaMatch = new ArrayList<BindingProperty>();
      if ( isIPhoneWithoutCookie )
      {
        computedBindings.remove( MgwtOsPropertyProvider.iPhone_undefined );
        nonRetinaMatch.add( MgwtOsPropertyProvider.iPhone );
        retinaMatch.add( MgwtOsPropertyProvider.retina );
      }
      else //if ( isIPadWithoutCookie )
      {
        computedBindings.remove( MgwtOsPropertyProvider.iPad_undefined );
        nonRetinaMatch.add( MgwtOsPropertyProvider.iPad );
        retinaMatch.add( MgwtOsPropertyProvider.iPad_retina );
      }

      nonRetinaMatch.addAll( computedBindings );
      retinaMatch.addAll( computedBindings );

      final String moduleNameNonRetina = getPermutationStrongName( baseUrl, moduleName, nonRetinaMatch );
      final String moduleNameRetina = getPermutationStrongName( baseUrl, moduleName, retinaMatch );

      if ( null != moduleNameNonRetina && null != moduleNameRetina )
      {
        final String manifest = loadAndMergeManifests( baseUrl, moduleName, moduleNameNonRetina, moduleNameRetina );
        serveStringManifest( response, manifest );
        return true;
      }
    }
    return false;
  }
  */
}
