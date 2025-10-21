package model;

/**
 * Класс для представления тега задачи
 */
public class Tag {
    private String name;
    
    public Tag(String name) {
        this.name = name.toLowerCase().trim();
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tag tag = (Tag) obj;
        return name.equals(tag.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return "#" + name;
    }
}