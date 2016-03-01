package com.inwx.model;

import java.io.Serializable;
import java.util.*;

public class Element implements Serializable
{
    public Element(  )
    {
    }

    public List<Element> getChildren( ){
	return new ArrayList<Element>( );
    }
}
