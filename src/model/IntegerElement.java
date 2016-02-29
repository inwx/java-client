package model;

import java.io.Serializable;
import java.util.*;

public class IntegerElement extends Element implements Serializable
{
    Integer value;

    public IntegerElement(  )
    {
    }

    public IntegerElement( Integer value )
    {
        this.value = value;
    }

    public String toString(  )
    {
        return value.intValue(  ) + "";
    }

    public Integer getValue() {
    	return value.intValue(  );
    }

    public List<Element> getChildren( ){
	return new ArrayList<Element>( );
    }
}
