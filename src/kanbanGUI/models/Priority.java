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

    };
    public static Priority toPriority(String priorityString){
        if(priorityString.equals("High"))
            return Priority.High;
        else if (priorityString.equals("Normal"))
            return Priority.Normal;
        else
            return Priority.Low;
    }
}
