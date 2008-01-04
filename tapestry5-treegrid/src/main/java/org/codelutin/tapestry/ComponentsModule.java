package org.codelutin.tapestry;

import org.apache.tapestry.ioc.Configuration;
import org.apache.tapestry.services.LibraryMapping;

public class ComponentsModule {

    public static void contributeComponentClassResolver(
            Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("cl", "org.codelutin.tapestry"));
    }

}
