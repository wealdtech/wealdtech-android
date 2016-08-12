package com.wealdtech.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wealdtech.android.components.R;

import java.text.Collator;
import java.util.*;

/**
 * An adapter that contains a list of nationalities.
 * Android does not provide this information natively so we store it in a resources file
 */
public class NationalityAdapter extends BaseAdapter
{
  private List<Nationality> nationalities;

  private Context context;

  public NationalityAdapter(final Context context)
  {
    this.context = context;

    nationalities = new ArrayList<>();

    final String[] countryCodes = Locale.getISOCountries();
    for (final String countryCode : countryCodes)
    {
      final Locale locale = new Locale("", countryCode);
      final int nationalityId = context.getResources().getIdentifier("nationality_" + locale.getISO3Country().toLowerCase(Locale.ENGLISH), "string", context.getPackageName());
      if (nationalityId != 0)
      {
        final String nationalityName = context.getResources().getString(nationalityId);
        nationalities.add(new Nationality(nationalityName, countryCode));
      }
    }

    final Collator collator = Collator.getInstance(Locale.getDefault());
    collator.setStrength(Collator.PRIMARY);
    Collections.sort(nationalities, new Comparator<Nationality>(){
      @Override
      public int compare(final Nationality left, final Nationality right)
      {
        return collator.compare(left.getName(), right.getName());
      }
    });
  }

  @Override
  public int getCount()
  {
    return nationalities.size();
  }

  @Override
  public Nationality getItem(final int position)
  {
    return nationalities.get(position);
  }

  @Override
  public long getItemId(final int position)
  {
    return position;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent)
  {
    final Nationality nationality = nationalities.get(position);

    final View view =
        convertView == null ? LayoutInflater.from(context).inflate(R.layout.nationality_adapter_item, parent, false) : convertView;

    final TextView name = (TextView)view.findViewById(R.id.nationality_adapter_item_name);
    name.setText(nationality.getName());

    return view;
  }

  /**
   * Provide the position for a given country code
   * @param code the ISO 2-letter country code
   * @return the position of the nationality in the list; will be {@code -1} if the code is unknown
   */
  public int positionForCode(final String code)
  {
    for (int i = 0; i < nationalities.size(); i++)
    {
      if (nationalities.get(i).getCode().equals(code))
      {
        return i;
      }
    }
    return -1;
  }

  public class Nationality
  {
    private final String name;
    private final String code;

    public Nationality(final String name, final String code)
    {
      this.name = name;
      this.code = code;
    }

    public String getName() { return name; }

    public String getCode() { return code; }
  }
}
