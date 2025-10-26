# Available Bus Options

The seat booking system now includes dropdown menus for bus selection with the following predefined options:

## Regular Service
- **BUS001** - Regular Service
- **BUS002** - Regular Service  
- **BUS003** - Regular Service

## Deluxe Service
- **DELUXE01** - Deluxe Service
- **DELUXE02** - Deluxe Service

## Express Service
- **EXPRESS01** - Express Service
- **EXPRESS02** - Express Service

## Premium Services
- **SLEEPER01** - Sleeper Coach
- **AC001** - AC Service
- **AC002** - AC Service

## Features

### Bus Selection Page (`/select-bus`)
- **Dropdown Menu**: Choose from predefined bus options organized by service type
- **Custom Input**: Enter any custom bus number to create new routes
- **Smart Validation**: Ensures either dropdown selection or custom input is provided
- **Quick Select Buttons**: Direct links to popular bus routes

### Seat Map Page (`/seatmap/{busNumber}`)
- **Bus Switching**: Change buses without leaving the booking page
- **Current Bus Indicator**: Shows "Current: {BUS_NUMBER}" in dropdown
- **No Duplicates**: Current bus is excluded from other options
- **Confirmation Dialog**: Warns users about losing selections when switching buses

### Technical Implementation
- **Frontend**: Bootstrap dropdowns with optgroups for organization
- **Backend**: Spring Boot controllers handle both dropdown and custom selections
- **Validation**: Client-side and server-side validation for bus number requirements
- **User Experience**: Smooth transitions and clear feedback messages

## Usage
1. Visit `/select-bus` to choose a bus
2. Select from dropdown OR enter custom bus number
3. View seat map and make bookings
4. Switch buses anytime using the dropdown in booking forms
