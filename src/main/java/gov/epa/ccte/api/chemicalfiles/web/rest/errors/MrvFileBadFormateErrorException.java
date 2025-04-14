package gov.epa.ccte.api.chemicalfiles.web.rest.errors;

public class MrvFileBadFormateErrorException extends RuntimeException {

    public MrvFileBadFormateErrorException() {
        super("mrv File not meeting the expected format");
    }
}
