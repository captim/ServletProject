package test.controller.processors;

import test.model.ProcessorResult;

import javax.servlet.http.HttpServletRequest;

public class ProcessorShowAllStudents extends Processor {
    public ProcessorShowAllStudents() {
        action = "showAllStudents";
    }

    @Override
    public ProcessorResult getResult(HttpServletRequest request) {
        return new ProcessorResult("/students", "", true);
    }
}
