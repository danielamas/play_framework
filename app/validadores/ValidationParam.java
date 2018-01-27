package validadores;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Produto;
import play.data.DynamicForm;
import play.data.validation.ValidationError;



public class ValidationParam {

	public void validate(DynamicForm formulario) {
		if(formulario != null) {
			Class _Class = Produto.class;
			Field[] attribs = _Class.getDeclaredFields();
			List<String> attName = new ArrayList<String>();
			for(Field f : attribs) {
				attName.add(f.getName());
			}

			Map<String, String> param = formulario.data();
			param.forEach((k,v) -> {
				if(!attName.contains(k)) {
					formulario.reject(new ValidationError("Atributos inválidos", k));
				}
			});
		}
	}
}
