package org.example.cache.service.serviceImpl;

import org.example.cache.service.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceImpl implements Service {
    @Override
    public List<String> run(String item, double value, Date date) {
        List<String> result = new ArrayList();
        for (int i = 0; i < value; i++) {
            result.add(item+"|"+value+"|"+date.toString());
        }
        return new ArrayList<>();
    }
}
