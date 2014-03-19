package signalprocesser.voronoi.tools;

import java.util.*;

import java.awt.*;
import javax.swing.*;

public class CountryListModel extends AbstractListModel implements ComboBoxModel {
    
    /* ***************************************************** */
    // Variables
    
    private JComboBox combobox;
    
    private String selectedcountry;
    private ArrayList<String> countries;
    
    /* ***************************************************** */
    // Constructor
    
    public CountryListModel(JComboBox _combobox, ArrayList<String> _countries) {
        this.combobox = _combobox;
        this.countries = _countries;
        if ( countries.size()>=1 ) {
            this.selectedcountry = formatHumanReadable( countries.get(0) );
        }
        
        // Set an appriopriate render
        combobox.setRenderer( new CountryListRender() );
    }
    
    /* ***************************************************** */
    // Methods
    
    public int getSize() {
        return countries.size();
    }
    public Object getElementAt(int index) {
        return formatHumanReadable( countries.get(index) );
    }
    public Object getSelectedItem() {
        return ( selectedcountry==null ? null : selectedcountry );
    }
    public void setSelectedItem(Object _selectedcountry) {
        this.selectedcountry = (String) _selectedcountry;
    }
    
    public String getSelectedCountry() {
        int index = combobox.getSelectedIndex();
        if ( index<0 || index>=countries.size() ) {
            return null;
        } else {
            return countries.get( index );
        }
    }
    
    /* ***************************************************** */
    // Private method to nicely format filename
    //  (i.e. to turn "Some_Country.txt" --> "Some Country")
    
    public static String formatHumanReadable(String filename) {
        int index = filename.lastIndexOf('.');
        if ( index>0 ) {
            filename = filename.substring(0, index);
        }
        return filename.replace('_', ' ');
    }
    
    /* ***************************************************** */
    // Combobox Render that doesn't care about an oversided string
    
    public static class CountryListRender extends javax.swing.plaf.basic.BasicComboBoxRenderer {
        public Dimension getSize() {
            Dimension dimension = super.getSize();
            dimension.width = -1;
            return dimension;
        }
        public Dimension getPreferredSize() {
            Dimension dimension = super.getPreferredSize();
            dimension.width = -1;
            return dimension;
        }
    }
    
    /* ***************************************************** */
}