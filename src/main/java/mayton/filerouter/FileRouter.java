package mayton.filerouter;

import java.nio.file.Path;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class FileRouter extends RecursiveTask<FileRouterResult> {

    public FileRouter(FileRouterTaskDefinition fileRouterTaskDefinition) {

    }

    @Override
    protected FileRouterResult compute() {

        return new FileRouterResult();
    }
}
