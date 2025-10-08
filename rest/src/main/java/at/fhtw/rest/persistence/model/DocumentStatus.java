package at.fhtw.rest.persistence.model;

public enum DocumentStatus {
    UPLOADED, // document has been uploaded, currently being scanned
    SCANNED,  // document has been scanned,  currently being indexed
    INDEXED,  // document has been indexed,  no further processing
}
