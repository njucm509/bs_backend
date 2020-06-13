package cn.edu.njucm.wp.bs.release.anonymity.delmainprop;

public class DelMainProperties {
    public static Object[][] delMainProperty(Object[][] obj, String property) {
        System.out.println("del " + property + "...");

        if (property == null) {
            return obj;
        }
        int count = 0;
        for (int j = 0; j < obj[0].length; j++) {
            if (String.valueOf(obj[0][j]).equals(property)) {
                count++;
            }
        }
        if (count == 0) {
            return obj;
        }
        System.out.println("start del " + property + "...");
        Object[][] o_handled = new Object[obj.length][obj[0].length - 1];
        int property_index = 0;
        for (int j = 0; j < obj[0].length; j++) {
            if (obj[0][j].equals(property)) {
                property_index = j;
                break;
            }
        }

        for (int i = 0; i < o_handled.length; i++) {
            for (int j = 0; j < o_handled[i].length; j++) {
                if (j >= property_index) {
                    o_handled[i][j] = obj[i][j + 1];
                } else {
                    o_handled[i][j] = obj[i][j];
                }
            }
        }

        System.out.println("del " + property + " success ...");

        return o_handled;
    }
}
