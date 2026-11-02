
package com.mycompany.mvctaller.dao;

import java.util.List;



public interface CrudDAO <T>{
  boolean insertar(T onjeto);
  boolean actualizar(T objeto);
  boolean eliminar (int id);
  T buscarporID (int id);
  List<T> listarTodos();
}