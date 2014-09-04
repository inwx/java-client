package model;

import java.io.Serializable;
import java.util.*;

public class DoubleElement extends Element implements Serializable
{
    Double value;

    public DoubleElement(  )
    {
    }

    public DoubleElement( Double value )
    {
        this.value = value;
    }

    public String toString(  )
    {
        return value.doubleValue() + "";
    }

    public Double getValue() {
    	return value.doubleValue();
    }

    public List<Element> getChildren( ){
	return new ArrayList<Element>( );
    }
}
