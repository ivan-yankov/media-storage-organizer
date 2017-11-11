package org.yankov.mso.datamodel.generic;

import java.time.Duration;

public class Piece {

    private Integer id;

    private Disc disc;
    private Short cdTrackOrder;

    private String title;
    private Artist performer;
    private Artist accompanimentPerfomer;
    private Artist author;
    private Artist arrangementAuthor;
    private Artist conductor;
    private Artist solist;
    private Duration duration;
    private String note;
    private Source source;

    private Record record;

}
