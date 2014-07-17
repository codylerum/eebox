package com.outjected.eebox;

import java.util.UUID;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;

public class RequestParameterProducer {

	@Produces
	@RequestParameter
	public Integer produceInteger(InjectionPoint ip, FacesContext fc) {
		try {
			return Integer.parseInt(getStringValue(ip, fc));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Produces
	@RequestParameter
	public Long produceLong(InjectionPoint ip, FacesContext fc) {
		try {
			return Long.parseLong(getStringValue(ip, fc));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Produces
	@RequestParameter
	public UUID produceUUID(InjectionPoint ip, FacesContext fc) {
		try {
			return UUID.fromString(getStringValue(ip, fc));
		} catch (Exception e) {
			return null;
		}
	}

	@Produces
	@RequestParameter
	public Boolean produceBoolean(InjectionPoint ip, FacesContext fc) {
		try {
			String stringValue = getStringValue(ip, fc);
			return stringValue != null ? Boolean.valueOf(stringValue) : null;
		} catch (Exception e) {
			return null;
		}
	}

	@Produces
	@RequestParameter
	public String produceString(InjectionPoint ip, FacesContext fc) {
		return getStringValue(ip, fc);
	}

	private String getStringValue(InjectionPoint ip, FacesContext fc) {
		String name = ip.getAnnotated().getAnnotation(RequestParameter.class)
				.value();
		if ("".equals(name)) {
			name = ip.getMember().getName();
		}
		return fc.getExternalContext().getRequestParameterMap().get(name);
	}
}