package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.Serializable;

public class ArrayElement extends Element implements Serializable
{
    List<Element> elements;

    public ArrayElement(  )
    {
    }

    public ArrayElement( Object[] array )
    {
        this.elements = new ArrayList<Element>(  );

        for ( Object o : array ){
            if ( o instanceof HashMap ){
                this.elements.add( new MapElement( (Map) o ) );
            }
            else if ( o instanceof String ){
                this.elements.add( new StringElement( (String) o ) );
            }
            else if ( o instanceof Integer ){
                this.elements.add( new IntegerElement( (Integer) o ) );
            }
            else if ( o.getClass(  ).isArray(  ) ){
                this.elements.add( new ArrayElement( (Object[]) o ) );
            }
        }
    }

    public Element[] getArray(  )
    {
        return elements.toArray( new Element[] {  } );
    }

    public String toString( ){
	String result = "[\n";

	for( Element element : elements ){
	    result += "    " + element.toString( ) + " \n";
	}

	result += "]\n";

	return result;
    }

    public List<Element> getChildren( ){
	List<Element> result = new ArrayList<Element>( );

	for( Element elem : elements ){
	    result.addAll( elem.getChildren( ) );
	    result.add( elem );
	}

	return result;
    }
}
