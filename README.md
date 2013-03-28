DeeplinkEngine
==============

This repository contains all the projects developed to crate a set of libraries 
and portlets to create a set of utilities for CMS systems to integrate tourism affiliate programs.

Projects are structured as follow:
* DeeplinkEngine: Java project with the main entities/master data model
* DeeplinkEngineXXXX: (Where XXX is the name of a provider) Java implementation to connect with a provider
	- DeeplinkEngineBooking: Java implementation to connect with booking affiliate program
	- DeeplinkEngineHotelopia: Java implementation to connect with booking affiliate program
* DeeplinkEngine-Portlets: Liferay's portlets
* DeeplinkEngineUML: Papyrus project for modeling
