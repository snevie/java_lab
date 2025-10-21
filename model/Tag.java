package model;

/**
 * Представление тегов задачи
 * Пример: #работа, #учеба, #личное
 */
public class Tag {
    private String name; // Название тега
    
    public Tag(String name) {
        this.name = name.toLowerCase().trim();
    }
    
    public String getName() {
        return name;
    }
    
    // Проверка схожества тегов
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