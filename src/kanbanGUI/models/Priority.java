package kanbanGUI.models;

public enum Priority {
    High {
        @Override public String toString() {
            return "High";
        }
    },
    Normal{
        @Override public String toString() {
            return "Normal";
        }
    },
    Low {
        @Override public String toString() {
            return "Low";
        }
    }

}
