package ui;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

public class AdminDashboard extends JFrame {
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final BillingDAO billingDAO = new BillingDAO();
    private final MedicalRecordDAO recordDAO = new MedicalRecordDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

    private JTextField searchField = new JTextField(20);
    private Map<String, TableRowSorter<DefaultTableModel>> sorters = new HashMap<>();
    private JTabbedPane tabs;

    public AdminDashboard(User admin) {
        setTitle("Admin Dashboard - " + (admin != null ? admin.getFullName() : ""));
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("ADMIN CONTROL", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(Color.green);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(new JLabel("🔍 Search: "));
        searchPanel.add(searchField);

        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);


        JButton btnBackToLogin = new JButton("Back to Login");
        btnBackToLogin.setBackground(new Color(228, 102, 102));
        btnBackToLogin.setForeground(Color.WHITE);
        btnBackToLogin.setFocusPainted(false);
        btnBackToLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBackToLogin.setBounds(600, 5, 50, 10);
        btnBackToLogin.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to log out and go back to the login screen?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });


        topPanel.add(btnBackToLogin, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);



        tabs = new JTabbedPane();
        tabs.addTab("Doctors", doctorsPanel());
        tabs.addTab("Patients", patientsPanel());
        tabs.addTab("Appointments", appointmentsPanel());
        tabs.addTab("Billing", billingPanel());
        tabs.addTab("Medical Records", medicalRecordsPanel());
        tabs.addTab("Prescriptions", prescriptionsPanel());
        tabs.addTab("Settings", settingsPanel());


        add(tabs, BorderLayout.CENTER);


        JLabel footer = new JLabel("Logged in as: " + (admin != null ? admin.getFullName() : ""), SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(footer, BorderLayout.SOUTH);


        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterActiveTab(); }
            public void removeUpdate(DocumentEvent e) { filterActiveTab(); }
            public void changedUpdate(DocumentEvent e) { filterActiveTab(); }
        });
    }
    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
    }

    private void filterActiveTab() {
        String text = searchField.getText().trim();
        String activeTab = tabs.getTitleAt(tabs.getSelectedIndex());
        TableRowSorter<DefaultTableModel> sorter = sorters.get(activeTab);
        if (sorter != null) {
            if (text.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        }
    }

    private String safe(Object o) { return o == null ? "" : o.toString(); }

    private JPanel doctorsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"DoctorID","UserID","Name","Identifier","Status","Location","Contact","AssignedSince","CreatedAt"}, 0);
        JTable table = new JTable(model);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorters.put("Doctors", sorter);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add"), update = new JButton("Update"), delete = new JButton("Delete"), refresh = new JButton("Refresh");
        bottom.add(add); bottom.add(update); bottom.add(delete); bottom.add(refresh);

        styleButton(add, new Color(0,123,255), Color.WHITE);
        styleButton(update, new Color(255,165,0), Color.WHITE);
        styleButton(delete, new Color(220,53,69), Color.WHITE);
        styleButton(refresh, new Color(108,117,125), Color.WHITE);
        panel.add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Doctor> list = doctorDAO.getAll();
            for (Doctor d : list) model.addRow(new Object[]{d.getDoctorID(), d.getUserID(), d.getName(), d.getIdentifier(), d.getStatus(), d.getLocation(), d.getContact(), d.getAssignedSince(), d.getCreatedAt()});
        });

        add.addActionListener(e -> {
            JTextField userId = new JTextField(), name = new JTextField(), identifier = new JTextField(), location = new JTextField(), contact = new JTextField();
            Object[] form = {"UserID:", userId, "Name:", name, "Identifier:", identifier, "Location:", location, "Contact:", contact};
            int ok = JOptionPane.showConfirmDialog(this, form, "Add Doctor", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    Doctor d = new Doctor();
                    d.setUserID(Integer.parseInt(userId.getText().trim()));
                    d.setName(name.getText().trim());
                    d.setIdentifier(identifier.getText().trim());
                    d.setLocation(location.getText().trim());
                    d.setContact(contact.getText().trim());
                    d.setStatus("Active");
                    d.setAssignedSince(new Date(System.currentTimeMillis()));
                    doctorDAO.add(d);
                    refresh.doClick();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage()); }
            }
        });

        update.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r == -1) { JOptionPane.showMessageDialog(this, "Select doctor"); return; }
            int id = (int) model.getValueAt(r,0);
            JTextField name = new JTextField(safe(model.getValueAt(r,2)));
            JTextField identifier = new JTextField(safe(model.getValueAt(r,3)));
            JTextField status = new JTextField(safe(model.getValueAt(r,4)));
            JTextField location = new JTextField(safe(model.getValueAt(r,5)));
            JTextField contact = new JTextField(safe(model.getValueAt(r,6)));
            Object[] form = {"Name:", name, "Identifier:", identifier, "Status:", status, "Location:", location, "Contact:", contact};
            int ok = JOptionPane.showConfirmDialog(this, form, "Update Doctor", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                Doctor d = new Doctor();
                d.setDoctorID(id);
                d.setName(name.getText().trim());
                d.setIdentifier(identifier.getText().trim());
                d.setStatus(status.getText().trim());
                d.setLocation(location.getText().trim());
                d.setContact(contact.getText().trim());
                doctorDAO.update(d);
                refresh.doClick();
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r == -1) { JOptionPane.showMessageDialog(this, "Select doctor"); return; }
            int id = (int) model.getValueAt(r,0); if (JOptionPane.showConfirmDialog(this, "Delete doctor #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){ doctorDAO.delete(id); refresh.doClick(); }
        });

        refresh.doClick();
        return panel;
    }

    private JPanel patientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"PatientID","UserID","FullName","Gender","DOB","CreatedAt"}, 0
        );
        JTable table = new JTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorters.put("Patients", sorter);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        styleButton(add, new Color(0,123,255), Color.WHITE);
        styleButton(update, new Color(255,165,0), Color.WHITE);
        styleButton(delete, new Color(220,53,69), Color.WHITE);
        styleButton(refresh, new Color(108,117,125), Color.WHITE);

        bottom.add(add); bottom.add(update); bottom.add(delete); bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);


        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Patient> list = patientDAO.getAll();
            for (Patient p : list) {
                model.addRow(new Object[]{
                        p.getPatientID(), p.getUserID(), p.getFullName(),
                        p.getGender(), p.getDob(), p.getCreatedAt()
                });
            }
        });


        add.addActionListener(e -> {
            JTextField userId = new JTextField();
            JTextField name = new JTextField();
            JTextField gender = new JTextField();
            JTextField dob = new JTextField("YYYY-MM-DD");
            Object[] form = {"UserID:", userId, "Full Name:", name, "Gender:", gender, "DOB:", dob};
            int ok = JOptionPane.showConfirmDialog(this, form, "Add Patient", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    int uid = Integer.parseInt(userId.getText().trim());
                    String fullName = name.getText().trim();
                    String gen = gender.getText().trim();
                    Date date = Date.valueOf(dob.getText().trim());
                    patientDAO.addProfile(uid, fullName, gen, date);
                    refresh.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
                }
            }
        });


        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select a patient first!");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            JTextField fullName = new JTextField(model.getValueAt(r, 2).toString());
            JTextField gender = new JTextField(model.getValueAt(r, 3).toString());
            JTextField dob = new JTextField(model.getValueAt(r, 4).toString());
            Object[] form = {"Full Name:", fullName, "Gender:", gender, "DOB (YYYY-MM-DD):", dob};
            int ok = JOptionPane.showConfirmDialog(this, form, "Update Patient", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    Patient p = new Patient();
                    p.setPatientID(id);
                    p.setFullName(fullName.getText().trim());
                    p.setGender(gender.getText().trim());
                    p.setDob(Date.valueOf(dob.getText().trim()));
                    patientDAO.update(p);
                    refresh.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });


        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select a patient to delete");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            if (JOptionPane.showConfirmDialog(this, "Delete this patient?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                patientDAO.delete(id);
                refresh.doClick();
            }
        });

        refresh.doClick();
        return panel;
    }


    private JPanel appointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"AppointmentID", "OrderNumber", "PatientID", "DoctorID", "Date", "Status", "TotalAmount", "PaymentMethod", "Notes"}, 0);
        JTable table = new JTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorters.put("Appointments", sorter);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        bottom.add(add);
        bottom.add(update);
        bottom.add(delete);
        bottom.add(refresh);
        styleButton(add, new Color(0,123,255), Color.WHITE);
        styleButton(update, new Color(255,165,0), Color.WHITE);
        styleButton(delete, new Color(220,53,69), Color.WHITE);
        styleButton(refresh, new Color(108,117,125), Color.WHITE);
        panel.add(bottom, BorderLayout.SOUTH);


        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Appointment> list = appointmentDAO.getAll();
            for (Appointment a : list) {
                model.addRow(new Object[]{
                        a.getAppointmentID(),
                        a.getOrderNumber(),
                        a.getPatientID(),
                        a.getDoctorID(),
                        a.getAppointmentDate(),
                        a.getStatus(),
                        a.getTotalAmount(),
                        a.getPaymentMethod(),
                        a.getNotes()
                });
            }
        });


        add.addActionListener(e -> {
            JTextField order = new JTextField();
            JTextField pid = new JTextField();
            JTextField did = new JTextField();
            JTextField date = new JTextField("2025-10-30 10:00:00");
            JTextField status = new JTextField("Scheduled");
            JTextField total = new JTextField("0");
            JTextField method = new JTextField("Cash");
            JTextField notes = new JTextField();

            Object[] form = {
                    "Order Number:", order,
                    "Patient ID:", pid,
                    "Doctor ID:", did,
                    "Date (yyyy-mm-dd hh:mm:ss):", date,
                    "Status:", status,
                    "Total Amount:", total,
                    "Payment Method:", method,
                    "Notes:", notes
            };

            if (JOptionPane.showConfirmDialog(this, form, "Add Appointment", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    Appointment a = new Appointment();
                    a.setOrderNumber(order.getText().trim());
                    a.setPatientID(Integer.parseInt(pid.getText().trim()));
                    a.setDoctorID(Integer.parseInt(did.getText().trim()));
                    a.setAppointmentDate(java.sql.Timestamp.valueOf(date.getText().trim()));
                    a.setStatus(status.getText().trim());
                    a.setTotalAmount(Double.parseDouble(total.getText().trim()));
                    a.setPaymentMethod(method.getText().trim());
                    a.setNotes(notes.getText().trim());
                    appointmentDAO.add(a);
                    refresh.doClick();
                    JOptionPane.showMessageDialog(this, "Appointment added successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ Error adding appointment: " + ex.getMessage());
                }
            }
        });


        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select an appointment to update");
                return;
            }

            try {
                int id = (int) model.getValueAt(r, 0);
                JTextField order = new JTextField(safe(model.getValueAt(r, 1)));
                JTextField pid = new JTextField(safe(model.getValueAt(r, 2)));
                JTextField did = new JTextField(safe(model.getValueAt(r, 3)));
                JTextField date = new JTextField(safe(model.getValueAt(r, 4)));
                JTextField status = new JTextField(safe(model.getValueAt(r, 5)));
                JTextField total = new JTextField(safe(model.getValueAt(r, 6)));
                JTextField method = new JTextField(safe(model.getValueAt(r, 7)));
                JTextField notes = new JTextField(safe(model.getValueAt(r, 8)));

                Object[] form = {
                        "Order Number:", order,
                        "Patient ID:", pid,
                        "Doctor ID:", did,
                        "Date (yyyy-mm-dd hh:mm:ss):", date,
                        "Status:", status,
                        "Total Amount:", total,
                        "Payment Method:", method,
                        "Notes:", notes
                };

                int ok = JOptionPane.showConfirmDialog(this, form, "Update Appointment", JOptionPane.OK_CANCEL_OPTION);
                if (ok == JOptionPane.OK_OPTION) {
                    Appointment a = new Appointment();
                    a.setAppointmentID(id);
                    a.setOrderNumber(order.getText().trim());
                    a.setPatientID(Integer.parseInt(pid.getText().trim()));
                    a.setDoctorID(Integer.parseInt(did.getText().trim()));
                    a.setAppointmentDate(java.sql.Timestamp.valueOf(date.getText().trim())); // ✅ Correct Timestamp
                    a.setStatus(status.getText().trim());
                    a.setTotalAmount(Double.parseDouble(total.getText().trim()));
                    a.setPaymentMethod(method.getText().trim());
                    a.setNotes(notes.getText().trim());
                    appointmentDAO.update(a);
                    refresh.doClick();
                    JOptionPane.showMessageDialog(this, "Appointment updated successfully!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error updating appointment: " + ex.getMessage());
            }
        });


        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select an appointment to delete");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            if (JOptionPane.showConfirmDialog(this, "Delete appointment #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                appointmentDAO.delete(id);
                refresh.doClick();
                JOptionPane.showMessageDialog(this, "Appointment deleted!");
            }
        });

        refresh.doClick();
        return panel;
    }




    private JPanel billingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{
                "BillingID", "PatientID", "PatientName", "Amount", "PaymentMethod", "MedicalRecordID", "CreatedAt"}, 0);
        JTable table = new JTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorters.put("Billing", sorter);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);


        JPanel bottom = new JPanel();
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        bottom.add(add);
        bottom.add(update);
        bottom.add(delete);
        bottom.add(refresh);
        styleButton(add, new Color(0,123,255), Color.WHITE);
        styleButton(update, new Color(255,165,0), Color.WHITE);
        styleButton(delete, new Color(220,53,69), Color.WHITE);
        styleButton(refresh, new Color(108,117,125), Color.WHITE);
        panel.add(bottom, BorderLayout.SOUTH);


        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Billing> list = billingDAO.getAll();
            for (Billing b : list) {
                model.addRow(new Object[]{
                        b.getBillingID(), b.getPatientID(), b.getPatientName(),
                        b.getAmount(), b.getPaymentMethod(), b.getMedicalRecordID(), b.getCreatedAt()
                });
            }
        });


        add.addActionListener(e -> {
            JTextField pid = new JTextField();
            JTextField pname = new JTextField();
            JTextField amt = new JTextField();
            JTextField pmethod = new JTextField();
            Object[] form = {
                    "PatientID:", pid,
                    "Patient Name:", pname,
                    "Amount:", amt,
                    "Payment Method:", pmethod
            };
            int ok = JOptionPane.showConfirmDialog(this, form, "Add Billing", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    Billing b = new Billing();
                    b.setPatientID(pid.getText().trim().isEmpty() ? null : Integer.valueOf(pid.getText().trim()));
                    b.setPatientName(pname.getText().trim());
                    b.setAmount(Double.parseDouble(amt.getText().trim()));
                    b.setPaymentMethod(pmethod.getText().trim());
                    billingDAO.add(b);
                    refresh.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid data: " + ex.getMessage());
                }
            }
        });


        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Please select a billing record to update.");
                return;
            }

            int billingID = (int) model.getValueAt(r, 0);
            JTextField pname = new JTextField(model.getValueAt(r, 2).toString());
            JTextField amt = new JTextField(model.getValueAt(r, 3).toString());
            JTextField pmethod = new JTextField(model.getValueAt(r, 4).toString());
            Object[] form = {
                    "Patient Name:", pname,
                    "Amount:", amt,
                    "Payment Method:", pmethod
            };

            int ok = JOptionPane.showConfirmDialog(this, form, "Update Billing", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    Billing b = new Billing();
                    b.setBillingID(billingID);
                    b.setPatientName(pname.getText().trim());
                    b.setAmount(Double.parseDouble(amt.getText().trim()));
                    b.setPaymentMethod(pmethod.getText().trim());
                    billingDAO.update(b);
                    refresh.doClick();
                    JOptionPane.showMessageDialog(this, "Billing record updated successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error updating billing: " + ex.getMessage());
                }
            }
        });


        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select a billing record to delete.");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            if (JOptionPane.showConfirmDialog(this, "Delete billing record #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                billingDAO.delete(id);
                refresh.doClick();
            }
        });


        refresh.doClick();
        return panel;
    }


    private JPanel medicalRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"MedicalRecordID", "PatientID", "Attribute1", "Attribute2", "Attribute3", "CreatedAt"}, 0);
        JTable table = new JTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorters.put("Medical Records", sorter);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        bottom.add(add);
        bottom.add(update);
        bottom.add(delete);
        bottom.add(refresh);

        styleButton(add, new Color(0,123,255), Color.WHITE);
        styleButton(update, new Color(255,165,0), Color.WHITE);
        styleButton(delete, new Color(220,53,69), Color.WHITE);
        styleButton(refresh, new Color(108,117,125), Color.WHITE);
        panel.add(bottom, BorderLayout.SOUTH);

        // 🔄 REFRESH — load all records (with PatientID)
        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<MedicalRecord> list = recordDAO.getAll();
            for (MedicalRecord m : list) {
                model.addRow(new Object[]{
                        m.getMedicalRecordID(),
                        m.getPatientID(),
                        m.getAttribute1(),
                        m.getAttribute2(),
                        m.getAttribute3(),
                        m.getCreatedAt()
                });
            }
        });

        // ➕ ADD — assign record to a specific patient
        add.addActionListener(e -> {
            JTextField patientIdField = new JTextField();
            JTextField a1 = new JTextField();
            JTextField a2 = new JTextField();
            JTextField a3 = new JTextField();
            Object[] form = {
                    "Patient ID:", patientIdField,
                    "Attribute1:", a1,
                    "Attribute2:", a2,
                    "Attribute3:", a3
            };
            int ok = JOptionPane.showConfirmDialog(this, form, "Add Medical Record", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    MedicalRecord m = new MedicalRecord();
                    m.setPatientID(Integer.parseInt(patientIdField.getText().trim()));
                    m.setAttribute1(a1.getText().trim());
                    m.setAttribute2(a2.getText().trim());
                    m.setAttribute3(a3.getText().trim());
                    recordDAO.add(m);
                    JOptionPane.showMessageDialog(this, "Medical record added!");
                    refresh.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Patient ID or input.");
                }
            }
        });

        // ✏️ UPDATE — modify attributes or patient ID
        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select a record to update");
                return;
            }

            int id = (int) model.getValueAt(r, 0);
            JTextField patientIdField = new JTextField(model.getValueAt(r, 1).toString());
            JTextField a1 = new JTextField(model.getValueAt(r, 2).toString());
            JTextField a2 = new JTextField(model.getValueAt(r, 3).toString());
            JTextField a3 = new JTextField(model.getValueAt(r, 4).toString());
            Object[] form = {
                    "Patient ID:", patientIdField,
                    "Attribute1:", a1,
                    "Attribute2:", a2,
                    "Attribute3:", a3
            };

            if (JOptionPane.showConfirmDialog(this, form, "Update Medical Record", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    MedicalRecord m = new MedicalRecord();
                    m.setMedicalRecordID(id);
                    m.setPatientID(Integer.parseInt(patientIdField.getText().trim()));
                    m.setAttribute1(a1.getText().trim());
                    m.setAttribute2(a2.getText().trim());
                    m.setAttribute3(a3.getText().trim());
                    recordDAO.update(m);
                    JOptionPane.showMessageDialog(this, "Updated successfully!");
                    refresh.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid data.");
                }
            }
        });

        // ❌ DELETE
        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select record to delete");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            if (JOptionPane.showConfirmDialog(this, "Delete record #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                recordDAO.delete(id);
                refresh.doClick();
            }
        });

        refresh.doClick();
        return panel;
    }



    private JPanel prescriptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"PrescriptionID", "AppointmentID", "Attribute1", "Attribute2", "Attribute3", "CreatedAt"}, 0);
        JTable table = new JTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorters.put("Prescriptions", sorter);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        bottom.add(add); bottom.add(update); bottom.add(delete); bottom.add(refresh);

        styleButton(add, new Color(0,123,255), Color.WHITE);
        styleButton(update, new Color(255,165,0), Color.WHITE);
        styleButton(delete, new Color(220,53,69), Color.WHITE);
        styleButton(refresh, new Color(108,117,125), Color.WHITE);
        panel.add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            model.setRowCount(0);
            for (Prescription p : prescriptionDAO.getAll()) {
                model.addRow(new Object[]{
                        p.getPrescriptionID(), p.getAppointmentID(),
                        p.getAttribute1(), p.getAttribute2(),
                        p.getAttribute3(), p.getCreatedAt()
                });
            }
        });

        add.addActionListener(e -> {
            JTextField appt = new JTextField();
            JTextField a1 = new JTextField();
            JTextField a2 = new JTextField();
            JTextField a3 = new JTextField();
            Object[] form = {"AppointmentID:", appt, "Attribute1:", a1, "Attribute2:", a2, "Attribute3:", a3};
            if (JOptionPane.showConfirmDialog(this, form, "Add Prescription", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                Prescription p = new Prescription();
                try { p.setAppointmentID(Integer.parseInt(appt.getText().trim())); }
                catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid appointment ID"); return; }
                p.setAttribute1(a1.getText().trim());
                p.setAttribute2(a2.getText().trim());
                p.setAttribute3(a3.getText().trim());
                prescriptionDAO.add(p);
                JOptionPane.showMessageDialog(this, "Prescription added!");
                refresh.doClick();
            }
        });

        update.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r == -1) { JOptionPane.showMessageDialog(this, "Select a record"); return; }
            int id = (int) model.getValueAt(r, 0);
            JTextField a1 = new JTextField(model.getValueAt(r, 2).toString());
            JTextField a2 = new JTextField(model.getValueAt(r, 3).toString());
            JTextField a3 = new JTextField(model.getValueAt(r, 4).toString());
            Object[] form = {"Attribute1:", a1, "Attribute2:", a2, "Attribute3:", a3};
            if (JOptionPane.showConfirmDialog(this, form, "Update Prescription", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                Prescription p = new Prescription();
                p.setPrescriptionID(id);
                p.setAttribute1(a1.getText().trim());
                p.setAttribute2(a2.getText().trim());
                p.setAttribute3(a3.getText().trim());
                prescriptionDAO.update(p);
                refresh.doClick();
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r == -1) { JOptionPane.showMessageDialog(this, "Select record to delete"); return; }
            int id = (int) model.getValueAt(r, 0);
            prescriptionDAO.delete(id);
            refresh.doClick();
        });

        refresh.doClick();
        return panel;
    }

    private JPanel settingsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        SystemSettingsDAO settingsDAO = new SystemSettingsDAO();

        JLabel lbl = new JLabel("Current Doctor Registration Code:");
        JTextField tfCode = new JTextField(settingsDAO.getDoctorCode(), 15);
        JButton btnUpdate = new JButton("Update Code");
        btnUpdate.setBackground(new Color(0, 123, 255));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);

        gbc.gridx=0; gbc.gridy=0; panel.add(lbl, gbc);
        gbc.gridx=1; panel.add(tfCode, gbc);
        gbc.gridy=1; gbc.gridx=1; panel.add(btnUpdate, gbc);

        btnUpdate.addActionListener(e -> {
            String newCode = tfCode.getText().trim();
            if (newCode.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Code cannot be empty!");
                return;
            }
            if (settingsDAO.updateDoctorCode(newCode)) {
                JOptionPane.showMessageDialog(panel, "✅ Doctor code updated successfully!");
            } else {
                JOptionPane.showMessageDialog(panel, "❌ Failed to update code!");
            }
        });

        return panel;
    }

}
