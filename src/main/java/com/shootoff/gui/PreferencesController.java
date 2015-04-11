/*
 * Copyright (c) 2015 phrack. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */

package com.shootoff.gui;

import java.io.IOException;

import com.shootoff.config.Configuration;
import com.shootoff.config.ConfigurationException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class PreferencesController {
	@FXML private Slider detectionRateSlider;
	@FXML private Label detectionRateLabel;
	@FXML private Slider laserIntensitySlider;
	@FXML private Label laserIntensityLabel;
	@FXML private Slider markerRadiusSlider;
	@FXML private Label markerRadiusLabel;
	@FXML private ChoiceBox<String> ignoreLaserColorChoiceBox;
	@FXML private CheckBox virtualMagazineCheckBox;
	@FXML private Slider virtualMagazineSlider;
	@FXML private Label virtualMagazineLabel;
	@FXML private CheckBox malfunctionsCheckBox;
	@FXML private Slider malfunctionsSlider;
	@FXML private Label malfunctionsLabel;
	
	private Stage preferencesStage;
	private Configuration config;
	
	public void setConfig(Configuration config) throws IOException {
	    preferencesStage = (Stage)detectionRateSlider.getScene().getWindow();
	    
	    ignoreLaserColorChoiceBox.setItems(FXCollections.observableArrayList(
	    		"None", "red", "green"));
	    
		this.config = config;
		
		linkSliderToLabel(detectionRateSlider, detectionRateLabel);
		linkSliderToLabel(laserIntensitySlider, laserIntensityLabel);
		linkSliderToLabel(markerRadiusSlider, markerRadiusLabel);
		linkSliderToLabel(virtualMagazineSlider, virtualMagazineLabel);
		linkSliderToLabel(malfunctionsSlider, malfunctionsLabel);
		
		detectionRateSlider.setValue(config.getDetectionRate());
		laserIntensitySlider.setValue(config.getLaserIntensity());
		markerRadiusSlider.setValue(config.getMarkerRadius());
		ignoreLaserColorChoiceBox.setValue(config.getIgnoreLaserColorName());
		virtualMagazineCheckBox.setSelected(config.useVirtualMagazine());
		virtualMagazineSlider.setValue(config.getVirtualMagazineCapacity());
		malfunctionsCheckBox.setSelected(config.useMalfunctions());
		malfunctionsSlider.setValue(config.getMalfunctionsProbability());
	}
	
	private void linkSliderToLabel(final Slider slider, final Label label) {
		slider.valueProperty().addListener(new ChangeListener<Number>() {
		      @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
		        if (newValue == null) {
		          label.setText("");
		          return;
		        }
		        label.setText(Math.round(newValue.intValue()) + "");
		      }
		    });
	}
	
	@FXML 
	public void virtualMagazineCheckBoxClicked(ActionEvent event) {
		virtualMagazineSlider.setDisable(!virtualMagazineCheckBox.isSelected());
	}

	@FXML 
	public void malfunctionsCheckBoxClicked(ActionEvent event) {
		malfunctionsSlider.setDisable(!malfunctionsCheckBox.isSelected());
	}
	
	@FXML 
	public void okClicked(ActionEvent event) throws ConfigurationException, IOException {
		config.setDetectionRate((int)detectionRateSlider.getValue());
		config.setLaserIntensity((int)laserIntensitySlider.getValue());
		config.setMarkerRadius((int)markerRadiusSlider.getValue());
		config.setIgnoreLaserColor(!ignoreLaserColorChoiceBox.getValue().equals("None"));
		config.setIgnoreLaserColorName(ignoreLaserColorChoiceBox.getValue());
		config.setUseVirtualMagazine(virtualMagazineCheckBox.isSelected());
		config.setVirtualMagazineCapacity((int)virtualMagazineSlider.getValue());
		config.setMalfunctions(malfunctionsCheckBox.isSelected());
		config.setMalfunctionsProbability((float)malfunctionsSlider.getValue());
		
		config.writeConfigurationFile();
		preferencesStage.close();
	}

	@FXML 
	public void cancelClicked(ActionEvent event) {
		preferencesStage.close();
	}
}
