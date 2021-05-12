package org.word.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "module")
public class ModulelList {
    private List<Modulel> list;

    public List<Modulel> getList() {
        return list;
    }

    public void setList(List<Modulel> list) {
        this.list = list;
    }
}
