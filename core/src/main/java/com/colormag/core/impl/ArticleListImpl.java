package com.colormag.core.impl;

import org.osgi.service.component.annotations.Component;

import com.colormag.core.services.ArticleInterface;


@Component(service = ArticleInterface.class, immediate = true)
public class ArticleListImpl implements ArticleInterface{

	@Override
	public void articlelisting() {
		// TODO Auto-generated method stub
		
	}

}
