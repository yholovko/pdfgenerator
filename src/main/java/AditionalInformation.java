//package com.watkins.model;

import java.util.Date;
import java.util.List;

public class AditionalInformation {
    //Link to the image
    private String logo;
    private String title;
    private String subtitle;
    private String authorName;
    private String promotinalHeadline;
    private List<String> salesPoints;
    private String synopsis;
    //Link to the image
    private String coverPicture;
    //Format will be numbers only. For example: 9781780289311
    private String isbn;
    //Format will be date only
    private Date publicationDate;
    //Format will be price only
    private Float price;
    private String category;
    private String binding;
    private String format;
    private String extent;
    private String illustrations;
    private String authorLocation;
    //about the author
    private String bio;
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getPromotinalHeadline() {
        return promotinalHeadline;
    }
    public void setPromotinalHeadline(String promotinalHeadline) {
        this.promotinalHeadline = promotinalHeadline;
    }
    public List<String> getSalesPoints() {
        return salesPoints;
    }
    public void setSalesPoints(List<String> salesPoints) {
        this.salesPoints = salesPoints;
    }
    public String getSynopsis() {
        return synopsis;
    }
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
    public String getCoverPicture() {
        return coverPicture;
    }
    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public Date getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }
    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getBinding() {
        return binding;
    }
    public void setBinding(String binding) {
        this.binding = binding;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    public String getExtent() {
        return extent;
    }
    public void setExtent(String extent) {
        this.extent = extent;
    }
    public String getIllustrations() {
        return illustrations;
    }
    public void setIllustrations(String illustrations) {
        this.illustrations = illustrations;
    }
    public String getAuthorLocation() {
        return authorLocation;
    }
    public void setAuthorLocation(String authorLocation) {
        this.authorLocation = authorLocation;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

}
