/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.atividadestestes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lucas
 */
public class ClassTested {
    private Collaborator listener;

    private final Map<String, String> documents =
            new HashMap<>();

    public void setListener(Collaborator listener) {
        this.listener = listener;
    }

    public void addDocument(String title, String content) {
        boolean documentAlreadyExists =
                documents.containsKey(title);

        documents.put(title, content);

        if (documentAlreadyExists) {
            listener.documentChanged(title);
        } else {
            listener.documentAdded(title);
        }
    }

    public boolean removeDocument(String title) {
        if (!documents.containsKey(title)) {
            return true;
        }

        if (listener.voteForRemoval(title) <= 0) {
            return false;
        }

        documents.remove(title);
        listener.documentRemoved(title);

        return true;
    }
    
    
}
