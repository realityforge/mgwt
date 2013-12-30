package com.googlecode.mgwt.linker.server;

import com.googlecode.mgwt.linker.server.propertyprovider.MgwtOsPropertyProvider;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.realityforge.gwt.appcache.server.AbstractManifestServlet;
import org.realityforge.gwt.appcache.server.BindingProperty;

public class Html5ManifestServletBase
  extends AbstractManifestServlet
{
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
}
