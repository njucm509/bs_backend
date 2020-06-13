package cn.edu.njucm.wp.bs.release.anonymity.fanhua;


public class GeneralizeSecondProperties {
    public static Object[][] generalizeSecondProperty(Object[][] obj, String property, int range) {
        Object[][] o_handled = new Object[obj.length][obj[0].length];
        boolean flag = false;
        if (property.toUpperCase().equals("AGE")) {
            int age_index = 0;
            for (int i = 0; i < obj[0].length; i++) {
                if (String.valueOf(obj[0][i]).toUpperCase().equals("AGE")) {
                    age_index = i;
                    break;
                }
            }
            for (int i = 0; i < obj.length; i++) {
                for (int j = 0; j < obj[0].length; j++) {
                    if (j == age_index && i != 0) {
                        if (obj[i][j] instanceof Integer) {
                            o_handled[i][j] = Generalization.ageGeneralization((Integer) obj[i][j], range);
                        } else {
                            o_handled[i][j] = Generalization.ageGeneralization(Integer.parseInt((String) obj[i][j]), range);
                        }
                    } else {
                        o_handled[i][j] = obj[i][j];
                    }
                }
            }
            flag = true;
        }

        if (property.equals("address")) {
            int address_index = 0;
            for (int i = 0; i < obj[0].length; i++) {
                if (obj[0][i].equals("address")) {
                    address_index = i;
                    break;
                }
            }
            for (int i = 0; i < obj.length; i++) {
                for (int j = 0; j < obj[0].length; j++) {
                    if (j == address_index && i != 0) {
                        o_handled[i][j] = Generalization.addressGeneralization((String) obj[i][j], range);
                    } else {
                        o_handled[i][j] = obj[i][j];
                    }
                }
            }
            flag = true;
        }

        if (flag) {
            System.out.println("generalize " + property + " success!");
        } else {
            System.out.println("generalize " + property + " fail!");
        }

        return o_handled;
    }
}
