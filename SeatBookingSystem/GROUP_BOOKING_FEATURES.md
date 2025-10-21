# Group Booking Feature

## Overview
The seat booking system has been extended to support group booking functionality, allowing users to book multiple seats at once for the same passenger.

## New Features Added

### 1. Backend Components

#### DTOs
- **GroupBookingRequest**: Handles group booking requests with multiple seat numbers
- **GroupBookingResponse**: Provides response with booking results and status

#### Service Layer
- **BookingService.createGroupBooking()**: Transactional method that:
  - Validates all selected seats are available
  - Books all seats atomically (all or none)
  - Creates individual booking records for each seat
  - Returns comprehensive response with success/failure details

#### Controllers
- **BookingController**: Added `/api/bookings/group` endpoint for REST API
- **SeatMapController**: Added `/seatmap/book-group` endpoint for web interface

### 2. Frontend Components

#### User Interface
- **Tabbed Interface**: Switch between single seat and group booking modes
- **Visual Seat Selection**: Click seats to select/deselect for group booking
- **Real-time Feedback**: Shows selected seats count and numbers
- **Clear Selection**: Button to reset seat selection

#### Seat States
- **Available** (Green): Can be selected for booking
- **Booked** (Red): Already booked, cannot be selected
- **Selected** (Blue): Currently selected for group booking

### 3. Key Features

#### Atomic Transactions
- All seats in a group booking are processed in a single transaction
- If any seat becomes unavailable during booking, the entire transaction is rolled back
- Ensures data consistency and prevents partial bookings

#### Validation
- Checks all seats are available before proceeding
- Validates passenger information
- Prevents booking of already booked seats

#### User Experience
- Intuitive seat selection with visual feedback
- Clear error messages for booking conflicts
- Success confirmation with booking details

## Usage

### Web Interface
1. Navigate to `/seatmap/{busNumber}` (e.g., `/seatmap/Bus123`)
2. Switch to "Group Booking (Multiple Seats)" tab
3. Fill in passenger details
4. Click on available seats to select them
5. Review selected seats in the summary
6. Click "Book Selected Seats" to complete booking

### REST API
```bash
POST /api/bookings/group
Content-Type: application/json

{
  "passengerName": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890",
  "busNumber": "Bus123",
  "seatNumbers": [1, 2, 3, 4]
}
```

## Technical Details

### Database Schema
- No changes to existing schema required
- Creates multiple booking records for group bookings
- Each seat gets its own booking entry with the same passenger details

### Error Handling
- Comprehensive validation of seat availability
- Clear error messages for booking conflicts
- Graceful handling of system errors

### Performance
- Optimized seat availability checking
- Bulk operations where possible
- Minimal database queries

## Browser Compatibility
- Requires modern browser with JavaScript support
- Bootstrap 5 for responsive design
- Works on desktop and mobile devices

