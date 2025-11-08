db = db.getSiblingDB('seatdb');
db.createCollection('seat_reservations');
db.seat_reservations.createIndex(
    { bookingId: 1, seatNumber: 1 },
    { unique: true, name: 'booking_seat_idx' }
);

