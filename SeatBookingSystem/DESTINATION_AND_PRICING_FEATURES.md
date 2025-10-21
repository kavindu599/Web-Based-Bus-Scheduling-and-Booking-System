# Destination and Pricing Features Implementation

## Overview
Successfully implemented origin/destination selection and dynamic pricing for both single and group booking forms.

## ✅ **New Features Added**

### 1. **Origin and Destination Selection**

**Database Fields:**
- Added `origin` and `destination` fields to `Booking` model
- Added `origin` and `destination` fields to `GroupBookingRequest` DTO
- Both fields are required with `@NotBlank` validation

**Available Cities and Routes:**
- **New York**: Boston, Philadelphia, Washington DC, Buffalo, Albany
- **Los Angeles**: San Francisco, Las Vegas, San Diego, Phoenix, Sacramento
- **Chicago**: Detroit, Milwaukee, Indianapolis, St. Louis, Minneapolis
- **Houston**: Dallas, Austin, San Antonio, New Orleans, Oklahoma City
- **Miami**: Orlando, Tampa, Jacksonville, Fort Lauderdale, West Palm Beach
- **Seattle**: Portland, Vancouver, Spokane, Tacoma, Bellingham
- **Denver**: Colorado Springs, Boulder, Fort Collins, Pueblo, Grand Junction
- **Atlanta**: Savannah, Augusta, Columbus, Macon, Albany
- **Boston**: New York, Portland ME, Worcester, Springfield, Hartford
- **Phoenix**: Los Angeles, Tucson, Flagstaff, Yuma, Prescott

### 2. **Dynamic Pricing System**

**PricingService Features:**
- **Base Price Calculation**: $25-$150 based on route hash
- **Bus Type Multipliers**:
  - Regular Service: 1.0x (base price)
  - Express: 1.2x (+20% premium)
  - Deluxe: 1.3x (+30% premium)
  - AC Service: 1.4x (+40% premium)
  - Sleeper: 1.5x (+50% premium)
- **Random Factor**: ±10% variation for realistic pricing
- **Route Validation**: Ensures valid origin-destination combinations

### 3. **Enhanced User Interface**

**Single Booking Form:**
```html
- Origin dropdown (required)
- Destination dropdown (dynamically populated)
- Real-time price display
- Form validation for route selection
```

**Group Booking Form:**
```html
- Origin dropdown (required)
- Destination dropdown (dynamically populated)
- Per-ticket price display
- Total price calculation (price × number of seats)
- Dynamic total updates when seats are selected/deselected
```

**Price Display Features:**
- Real-time price fetching via REST API
- Formatted currency display ($XX.XX)
- Total calculation for group bookings
- Price updates when bus type changes

### 4. **REST API Endpoints**

**New Endpoints:**
- `GET /seatmap/api/destinations/{origin}` - Get available destinations for an origin
- `GET /seatmap/api/price/{origin}/{destination}/{busNumber}` - Get ticket price for route

### 5. **Enhanced Booking Confirmations**

**Single Booking Success:**
```
"Booking confirmed for seat 15 on bus BUS001. 
Route: New York to Boston. 
Ticket Price: $67.45"
```

**Group Booking Success:**
```
"Successfully booked 3 seats: [12, 13, 14] for John Doe. 
Route: Los Angeles to San Francisco. 
Total Amount: $234.60 ($78.20 per ticket)"
```

## 🔧 **Technical Implementation**

### Frontend JavaScript Functions:
- `updateSingleDestinations()` - Populates destination dropdown for single booking
- `updateSinglePrice()` - Fetches and displays price for single booking
- `updateGroupDestinations()` - Populates destination dropdown for group booking
- `updateGroupPrice()` - Fetches and displays price for group booking
- `updateGroupTotalPrice()` - Calculates total price for multiple seats

### Backend Services:
- **PricingService**: Handles route validation and price calculation
- **BookingService**: Updated to include pricing and route validation
- **SeatMapController**: Enhanced with REST endpoints and pricing integration

### Validation Layers:
1. **Client-Side**: JavaScript form validation before submission
2. **HTML5**: Required attributes and pattern validation
3. **Server-Side**: Spring validation annotations
4. **Business Logic**: Route validation and pricing rules

## 💰 **Pricing Examples**

### Sample Route Prices:
- **New York → Boston** (Regular Bus): ~$67.45
- **Los Angeles → San Francisco** (Express): ~$94.32
- **Chicago → Detroit** (Deluxe): ~$89.67
- **Miami → Orlando** (AC Service): ~$76.88
- **Seattle → Portland** (Sleeper): ~$112.50

### Bus Type Premium Examples:
- **Regular BUS001**: Base price
- **EXPRESS01**: +20% premium
- **DELUXE01**: +30% premium
- **AC001**: +40% premium
- **SLEEPER01**: +50% premium

## 🎯 **User Experience**

### Booking Flow:
1. **Select Origin**: Choose departure city from dropdown
2. **Select Destination**: Available destinations populate automatically
3. **View Price**: Real-time price display appears
4. **Select Seats**: For group booking, total price updates dynamically
5. **Complete Booking**: Confirmation includes route and pricing details

### Validation Messages:
- "Please select origin city."
- "Please select destination city."
- "Invalid route from [Origin] to [Destination]"
- Real-time price updates without page refresh

## 📊 **Benefits**

### Business Value:
- **Dynamic Pricing**: Maximizes revenue based on route demand
- **Route Management**: Validates legitimate travel routes
- **Premium Services**: Higher margins for luxury bus types
- **User Experience**: Clear pricing transparency

### Technical Benefits:
- **Scalable Architecture**: Easy to add new cities and routes
- **API-Driven**: Clean separation between frontend and backend
- **Validation**: Multiple layers prevent invalid bookings
- **Performance**: Efficient price calculation and caching

## 🧪 **Testing Scenarios**

### Valid Bookings:
- ✅ New York → Boston on BUS001
- ✅ Los Angeles → San Francisco on EXPRESS01
- ✅ Group booking: 3 seats Chicago → Detroit on DELUXE01

### Invalid Scenarios:
- ❌ Same origin and destination
- ❌ Invalid route (e.g., New York → Los Angeles - not in route map)
- ❌ Missing origin or destination
- ❌ Price calculation with invalid parameters

## 🚀 **Future Enhancements**

### Potential Additions:
- **Seasonal Pricing**: Different rates for peak/off-peak seasons
- **Distance-Based Pricing**: More accurate pricing based on actual distances
- **Dynamic Routes**: Admin interface to add/modify routes
- **Fare Classes**: Economy, Business, First Class options
- **Discounts**: Student, Senior, Group discounts
- **Real-Time Availability**: Integration with external booking systems
