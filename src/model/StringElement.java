package model;

import java.io.Serializable;
import java.util.*;

public class StringElement extends Element implements Serializable
{
    String value;

    public StringElement(  )
    {
    }

    public StringElement( String value )
    {
        this.value = value;
    }

    public String toString(  )
    {
        return value;
    }

    public void setValue( String value ){
	this.value = value;
    }

    public List<Element> getChildren( ){
	return new ArrayList<Element>( );
    }
}
