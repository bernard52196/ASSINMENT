from collections import deque

class MovieTheaterBookingSystem:
    def __init__(self):
        self.available_screenings = []  # List to manage available movie screenings
        self.ticket_requests = deque()  # Queue for ticket requests
        self.booking_history = []  # Stack to store booking history for undo functionality
    
    
    def add_screening(self, movie_name, time):
        screening = {'movie': movie_name, 'time': time}
        self.available_screenings.append(screening)
        print(f"Screening of '{movie_name}' at {time} has been added.")
    

    def remove_screening(self, movie_name, time):
        screening = {'movie': movie_name, 'time': time}
        if screening in self.available_screenings:
            self.available_screenings.remove(screening)
            print(f"Screening of '{movie_name}' at {time} has been removed.")
        else:
            print(f"Screening of '{movie_name}' at {time} not found.")
    
    def request_ticket(self, customer_name, movie_name, time):
        screening = {'movie': movie_name, 'time': time}
        if screening in self.available_screenings:
            self.ticket_requests.append((customer_name, screening))  # Add to the queue
            self.booking_history.append((customer_name, screening))  # Add booking to history (stack)
            print(f"Ticket requested for {customer_name} to '{movie_name}' at {time}.")
        else:
            print(f"Screening of '{movie_name}' at {time} not available.")
    
    def undo_last_booking(self):
        if self.booking_history:
            last_booking = self.booking_history.pop() 
            customer_name, screening = last_booking
            self.ticket_requests.remove(last_booking)
            print(f"Last booking for {customer_name} to '{screening['movie']}' at {screening['time']}' has been undone.")
        else:
            print("No bookings to undo.")
    def show_available_screenings(self):
        if self.available_screenings:
            print("Available movie screenings:")
            for screening in self.available_screenings:
                print(f"Movie: {screening['movie']}, Time: {screening['time']}")
        else:
            print("No screenings available.")
    def show_ticket_requests(self):
        if self.ticket_requests:
            print("Ticket requests:")
            for request in self.ticket_requests:
                customer_name, screening = request
                print(f"Customer: {customer_name}, Movie: {screening['movie']}, Time: {screening['time']}")
        else:
            print("No ticket requests.")


theater_system = MovieTheaterBookingSystem()

theater_system.add_screening("Dragon", "19:30 PM")
theater_system.add_screening("killaman", "21:00 PM")
theater_system.add_screening("Bamenya", "22:00 PM")


theater_system.show_available_screenings()

theater_system.request_ticket("Evode", "Dragon", "19:30 PM")
theater_system.request_ticket("Vital", "killaman", "21:00 PM")
theater_system.request_ticket("Adolphe", "Bamenya", "22:00 PM")


theater_system.show_ticket_requests()

theater_system.undo_last_booking()
theater_system.show_available_screenings()
theater_system.show_ticket_requests()
