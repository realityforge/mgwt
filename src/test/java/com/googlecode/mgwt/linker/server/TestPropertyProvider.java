package com.googlecode.mgwt.linker.server;

import javax.servlet.http.HttpServletRequest;
import org.realityforge.gwt.appcache.server.propertyprovider.PropertyProvider;

final class TestPropertyProvider
  implements PropertyProvider
{
  private final String _key;
  private final String _value;

  TestPropertyProvider( final String key, final String value )
  {
    _key = key;
    _value = value;
  }

  @Override
  public String getPropertyName()
  {
    return _key;
  }

  @Override
  public String getPropertyValue( final HttpServletRequest request )
    throws Exception
  {
    return _value;
  }
}